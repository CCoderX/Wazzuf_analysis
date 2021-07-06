import org.apache.commons.csv.CSVFormat;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import smile.data.DataFrame;
import smile.data.measure.NominalScale;
import smile.data.vector.BaseVector;
import smile.data.vector.IntVector;
import smile.io.Read;
import smile.io.Write;
import smile.plot.swing.Canvas;
import smile.plot.swing.Histogram;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SMILE_Analysis_class {
    private  int[] encodingYearsExp(DataFrame df, String col){
        String[] values= df.stringVector(col).distinct().toArray(new String[]{});
        int[] intValues=df.stringVector(col).factorize(new NominalScale((values))).toIntArray();
        return intValues;
    }
    //used to factorize yearsExp from string to int values
    public DataFrame factorizeYearsExp(DataFrame df) {
        return df.merge(IntVector.of("YearsExpEncoding",encodingYearsExp(df,"YearsExp")));
    }
    //used to read csv file
    public DataFrame readCSV(){
        CSVFormat format=CSVFormat.DEFAULT.withFirstRecordAsHeader();
        DataFrame df=null;

        try {
            df= Read.csv("Wuzzuf_Jobs.csv",format);

            System.out.println(df.structure());
//            System.out.println(df);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return df;
    }
    //used to display the summary of data frame
    public void displayInfo(DataFrame d){
        System.out.println(d.summary());
    }

    //used to count the occurrences of each unique values in selected column and
    // return map with key unique value and value #of occurrences sorted in ascending order
//    public Map<String, Long> getNumberOfOccurrencesOfColumn(String colName,DataFrame df) {
//        Map<String, Long> map = Arrays.stream((df.column(colName).toStringArray())).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();
//        map.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByValue())
//                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
//        return  sortedMap;
//    }
    //////////////

//    used to count the occurrences of each unique values in selected column and
//     return map with key unique value and value #of occurrences sorted in ascending order
    public Map<String, Long> calculateJobsPerCompany(List<Job> jobs) {
        Map<String, Long> map = jobs.stream().map(v->v.Company).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return  sortedMap;
    }
    /////////////
    //    used to count the occurrences of each job and
//     return map with key job and value #of occurrences sorted in ascending order
    public Map<String, Long> getTopJobs(List<Job> jobs) {
        Map<String, Long> map = jobs.stream().map(v->v.Tile).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return  sortedMap;
    }
    /////////////
    //    used to count the occurrences of each job and
//     return map with key job and value #of occurrences sorted in ascending order
    public Map<String, Long> getTopCities(List<Job> jobs) {
        Map<String, Long> map = jobs.stream().map(v->v.Country).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return  sortedMap;
    }
    /////////////

    //used to display map from start index till the end
    public Map<String,Long> displayMap(Map<String,Long> map,int Start ){
        int i = 0;
        Map<String,Long> result = new HashMap<>();
        for (Map.Entry<String,Long> entry : map.entrySet()) {
            if (i >= Start) {
                System.out.println("Title = " + entry.getKey() +
                        "| Occurrences = " + entry.getValue());
                result.put(entry.getKey(),entry.getValue());
            }
            i++;
        }
        return result;
    }

    //used to plot bar chart from map
    public void displayBarChart(Map<String,Long> map,String Name,String xTitle,String yTitle) {
        ArrayList<String> names = new ArrayList<String>(map.keySet());
        ArrayList<Long> values= new ArrayList<Long>(map.values());
        CategoryChart chart= new CategoryChartBuilder().width(1024).height(600).title(Name).xAxisTitle(xTitle).yAxisTitle(yTitle).build();

        chart.addSeries(Name,names,values);

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        new SwingWrapper(chart).displayChart();
    }

    //used to plot bie chart from map
    public void displayBieChart(Map<String,Long> map,String Name) {
        ArrayList<String> names = new ArrayList<String>(map.keySet());
        ArrayList<Long> values= new ArrayList<Long>(map.values());
        PieChart pie = new PieChartBuilder().width(512).height(512).title(Name).build();

        for(int i =0;i<names.size();i++) {
            pie.addSeries(names.get(i),values.get(i));
        }
        new SwingWrapper(pie).displayChart();
    }

    //takes list of jobs and return only unique and non null jobs
    public List<Job> filterJobs(DataFrame df) {
        List<Job> jobs = df.stream().map(row->new Job(row)).collect(Collectors.toList());
        List<Job> filtered_jobs = new ArrayList<>();
//        System.out.println(jobs.size());
        for(int i =0;i<jobs.size();i++) {
            if(jobs.get(i).hasNull()) {
                i++;

            }
            else {
                for (int j = 0; j < i; j++) {
                    if (jobs.get(i).equals(jobs.get(j))) {
                       i++;
                    }
                }
                filtered_jobs.add(jobs.get(i));
            }
        }
//        System.out.println(filtered_jobs.size());
        return filtered_jobs;
    }

    //return the occurrence of each skill mentioned in each job description
    //it extract the skills string and split it with ',' separator then append each skill to map<skills,occurrences>
    public Map<String,Long> getTopSkills(List<Job> jobs){
//        BaseVector skills = df.column("Skills");
        Map<String,Long> skillsDemand = new HashMap<>();
        for(int i =0;i<jobs.size();i++) {
            String[] names = jobs.get(i).Skills.split(",");
            for (String name:names) {
                if (skillsDemand.containsKey(name)) {
                    Long old = skillsDemand.get(name);
                    skillsDemand.put(name, old + 1);
                } else {
                    skillsDemand.put(name, 1L);
                }
            }
        }
        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();
        skillsDemand.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return  sortedMap;
    }
}
