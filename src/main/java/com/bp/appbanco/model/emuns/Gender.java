package com.bp.appbanco.model.emuns;

public enum Gender {
  MALE("M"), FEMALE("F");
  private final String abbreviation;
  Gender(String abbreviation) {
    this.abbreviation = abbreviation;
  }
  public String getAbbreviation() {
    return abbreviation;
  }
  
  public static Gender genderFromAbbreviation(String abbreviation){
    if(abbreviation.equals(MALE.abbreviation)){
      return MALE;
    }
    if(abbreviation.equals(FEMALE.abbreviation)){
      return FEMALE;
    }
    return null;
  }
}
