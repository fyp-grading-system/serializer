package com.fypgradingsystem.serializer.model;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Grading {
  @CsvBindByName(column = "jury_name")
  private String juryName;
  @CsvBindByName(column = "grading_topic")
  private String gradingTopic;
  @CsvBindByName(column = "topic")
  private String topic;
  @CsvBindByName(column = "member_1_grade")
  private String member1Grade;
  @CsvBindByName(column = "member_2_grade")
  private String member2Grade;
  @CsvBindByName(column = "member_3_grade")
  private String member3Grade;
}
