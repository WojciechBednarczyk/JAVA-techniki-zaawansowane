package PersonalData;

import lombok.*;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.Period;

@Data
public class PersonalData {

   @Setter(AccessLevel.NONE)
   private final String  id;
   private final String name;
   private final String surname;
   private int age;
   private final LocalDate dateOfBirth;
   private final String pesel;
   private final BufferedImage image;
   private String status;

   public PersonalData(String id, String name, String surname, LocalDate dateOfBirth, String pesel, BufferedImage image,String status) {
      this.id = id;
      this.name = name;
      this.surname = surname;
      this.dateOfBirth = dateOfBirth;
      this.pesel = pesel;
      this.image = image;
      this.status = status;
      age= Period.between(dateOfBirth,LocalDate.now()).getYears();
   }
}
