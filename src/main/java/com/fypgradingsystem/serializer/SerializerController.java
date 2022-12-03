package com.fypgradingsystem.serializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fypgradingsystem.serializer.messaging.Message;
import com.fypgradingsystem.serializer.messaging.QueueSender;
import com.fypgradingsystem.serializer.model.Grading;
import com.fypgradingsystem.serializer.model.Wrapper;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

@RestController
public class SerializerController {

  @Autowired
  QueueSender queueSender;

  @PostMapping("/serialize")
  public String uploadCSVFile(@RequestParam("file") MultipartFile file) {

    // validate file
    try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

      // create csv bean reader
      CsvToBean<Wrapper> csvToBean = new CsvToBeanBuilder<Wrapper>(reader)
          .withSeparator(',')
          .withType(Wrapper.class)
          .withIgnoreLeadingWhiteSpace(true)
          .build();

      // convert `CsvToBean` object to list of Wrappers
      List<Wrapper> wrappers = csvToBean.parse();

      System.out.println(Json.encode(wrappers));

      wrappers.forEach(wrapper -> {
        var teamUUID = UUID.randomUUID().toString();
        var teamJson = new JsonObject();
        teamJson.put("teamUUID", teamUUID);
        teamJson.put("subject", wrapper.getSubject());

        var student1 = new JsonObject();
        student1.put("name", wrapper.getStudent1().split(":")[0]);
        student1.put("email", wrapper.getStudent1().split(":")[1]);
        student1.put("major", wrapper.getStudent1().split(":")[2]);

        var student2 = new JsonObject();
        student2.put("name", wrapper.getStudent2().split(":")[0]);
        student2.put("email", wrapper.getStudent2().split(":")[1]);
        student2.put("major", wrapper.getStudent2().split(":")[2]);

        var student3 = new JsonObject();
        student3.put("name", wrapper.getStudent3().split(":")[0]);
        student3.put("email", wrapper.getStudent3().split(":")[1]);
        student3.put("major", wrapper.getStudent3().split(":")[2]);

        teamJson.put("members", List.of(student1, student2, student3));

        teamJson.put("supervisor", wrapper.getSupervisor());
        Message teamMessage = Message
            .builder()
            .type("create-squad")
            .queue("squads")
            .order(Json.encode(teamJson))
            .build();

        queueSender.send(teamMessage);

        var juryJson = new JsonObject();

        juryJson.put("teamUUID", teamUUID);
        juryJson.put("name", wrapper.getJury1().split(":")[0]);
        juryJson.put("email", wrapper.getJury1().split(":")[1]);

        Message jury1Message = Message
            .builder()
            .type("create-jury")
            .queue("referees")
            .order(Json.encode(juryJson))
            .build();

        queueSender.send(jury1Message);

        var jury2Json = new JsonObject();

        jury2Json.put("teamUUID", teamUUID);
        jury2Json.put("name", wrapper.getJury2().split(":")[0]);
        jury2Json.put("email", wrapper.getJury2().split(":")[1]);

        Message jury2Message = Message
            .builder()
            .type("create-jury")
            .queue("referees")
            .order(Json.encode(juryJson))
            .build();

        queueSender.send(jury2Message);

        var jury3Json = new JsonObject();

        jury3Json.put("teamUUID", teamUUID);
        jury3Json.put("name", wrapper.getJury3().split(":")[0]);
        jury3Json.put("email", wrapper.getJury3().split(":")[1]);

        Message jury3Message = Message
            .builder()
            .type("create-jury")
            .queue("referees")
            .order(Json.encode(juryJson))
            .build();

        queueSender.send(jury3Message);

      });

      // TODO: save users in DB?

    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }

    return "file-upload-status";
  }

  @PostMapping("/submit-grades")
  public String submitCsvFile(@RequestParam("file") MultipartFile file) {
    try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

      // create csv bean reader
      CsvToBean<Grading> csvToBean = new CsvToBeanBuilder<Grading>(reader)
          .withSeparator(',')
          .withType(Grading.class)
          .withIgnoreLeadingWhiteSpace(true)
          .build();

      // convert `CsvToBean` object to list of Gradings
      List<Grading> grading = csvToBean.parse();

      grading.forEach(grade -> {
        var gradingJson = new JsonObject();
        gradingJson.put("juryName", grade.getJuryName());

        Message gradingMessage = Message
            .builder()
            .type("submit-grade")
            .queue("grades")
            .order(Json.encode(gradingJson))
            .build();

        queueSender.send(gradingMessage);
      });

    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    return "file-upload-status";
  }
}
