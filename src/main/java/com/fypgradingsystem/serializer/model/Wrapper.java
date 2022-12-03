package com.fypgradingsystem.serializer.model;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wrapper {
  @CsvBindByName
  private String subject;
  @CsvBindByName
  private String description;
  @CsvBindByName
  private String student1;
  @CsvBindByName
  private String student2;
  @CsvBindByName
  private String student3;
  @CsvBindByName
  private String supervisor;
  @CsvBindByName
  private String jury1;
  @CsvBindByName
  private String jury2;
  @CsvBindByName
  private String jury3;
}
