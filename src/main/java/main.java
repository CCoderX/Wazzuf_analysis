import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.type.StructType;
import smile.data.vector.*;
import smile.data.vector.Vector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class main {
    public static void main(String[] args){

        SMILE_Analysis_class s = new SMILE_Analysis_class();
        DataFrame df = s.readCSV();
        System.out.println(df.summary());
        System.out.println(df.size());
        List<Job> jobs = s.filterJobs(df);

        System.out.println(jobs.size());
        //////////////////////////////
        System.out.println("Top companies :");
        //Map<String,Long> companiesJobs = s.getNumberOfOccurrencesOfColumn("Company",df);
        Map<String,Long> companiesJobs = s.calculateJobsPerCompany(jobs);
        Map<String,Long> selectedCompanies = s.displayMap(companiesJobs,companiesJobs.size()-10);
        s.displayBieChart(selectedCompanies,"companiesJobs");
        ///////////////////////
        //////////////////////
        /////////////////////
        System.out.println("Top jobs :");
//        Map<String,Long> jobsDemand = s.getNumberOfOccurrencesOfColumn("Title",df);
        Map<String,Long> jobsDemand = s.getTopJobs(jobs);
        Map<String,Long> selectedJobs = s.displayMap(jobsDemand,jobsDemand.size()-10);
        s.displayBarChart(selectedJobs,"Job Demand","Jobs", "Vaccines");
        /////////////////////////
        /////////////////////////
        System.out.println("Top Cities :");
//        Map<String,Long> areas = s.getNumberOfOccurrencesOfColumn("Location",df);
        Map<String,Long> areas = s.getTopCities(jobs);
        Map<String,Long> selectedAreas = s.displayMap(areas,areas.size()-10);
        s.displayBarChart(selectedAreas,"Areas","Areas", "Vaccines");
        //////////////////////
        //////////////////////
        System.out.println("Top 10 Skills :");
        Map<String,Long> skills = s.getTopSkills(jobs);
        s.displayMap(skills,skills.size()-10);
        ///////
        ///////
        System.out.println("Factorize Years Exp");
        df = s.factorizeYearsExp(df);
        System.out.println(df);
    }
}
