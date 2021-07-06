import smile.data.Tuple;
import smile.data.type.StructType;

import java.util.Locale;

public class Job {
    String Tile , Company , Location,Type ,Level, YearsExp ,Country,Skills;
    public Job(smile.data.Tuple tuple){
        Tile = (String) tuple.get("Title");
        Company = (String) tuple.get("Company");
        Location = (String) tuple.get("Location");
        Type = (String) tuple.get("Type");
        Level = (String) tuple.get("Level");
        YearsExp = (String) tuple.get("YearsExp");
        Country = (String) tuple.get("Country");
        Skills = (String) tuple.get("Skills");
    }

    public Job(String tile, String company, String location, String type, String level, String yearsExp, String country, String skills) {
        Tile = tile;
        Company = company;
        Location = location;
        Type = type;
        Level = level;
        YearsExp = yearsExp;
        Country = country;
        Skills = skills;
    }
    public Boolean hasNull() {
        return (Tile.toLowerCase().contains("null") || Company.toLowerCase().contains("null") ||
                Location.toLowerCase().contains("null") ||Type.toLowerCase().contains("null") ||
                Level.toLowerCase().contains("null") || YearsExp.toLowerCase().contains("null")
                || Country.toLowerCase().contains("null")|| Skills.toLowerCase().contains("null"));
    }
    public Boolean equals(Job j) {
        return (Tile.equals(j.Tile) && Company.equals((j.Company)) &&
                Location.equals(j.Location) && Type.equals((j.Type)) &&
                        Level.equals(j.Level)&&  YearsExp.equals(j.YearsExp) &&
                        Country.equals(j.Country) && Skills.equals(j.Skills)
                );
    }
}
