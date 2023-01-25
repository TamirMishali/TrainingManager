package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import static androidx.room.ForeignKey.NO_ACTION;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Comparator;

// todo: (LOW) add under all Classes the @Ignore thing above all constructors that are not the main constructors.
//  use only the constructor with id in it.

@Entity(tableName = "exerciseabs_table",
        foreignKeys = {
            @ForeignKey(
                    entity = ExerciseAbstractInfoValue.class,
                    parentColumns = "id",
                    childColumns = "id_muscle",
                    onDelete = NO_ACTION
            ),
            @ForeignKey(
                    entity = ExerciseAbstractOperation.class,
                    parentColumns = "id",
                    childColumns = "id_operation",
                    onDelete = NO_ACTION
            ),
                @ForeignKey(
                    entity = ExerciseAbstractNickname.class,
                    parentColumns = "id",
                    childColumns = "id_nickname",
                    onDelete = NO_ACTION
            ),
                @ForeignKey(
                    entity = ExerciseAbstractInfoValue.class,
                    parentColumns = "id",
                    childColumns = "id_load_type",
                    onDelete = NO_ACTION
            ),
                @ForeignKey(
                    entity = ExerciseAbstractInfoValue.class,
                    parentColumns = "id",
                    childColumns = "id_position",
                    onDelete = NO_ACTION
            ),
                @ForeignKey(
                    entity = ExerciseAbstractInfoValue.class,
                    parentColumns = "id",
                    childColumns = "id_angle",
                    onDelete = NO_ACTION
            ),
                @ForeignKey(
                    entity = ExerciseAbstractInfoValue.class,
                    parentColumns = "id",
                    childColumns = "id_grip_width",
                    onDelete = NO_ACTION
            ),
                @ForeignKey(
                    entity = ExerciseAbstractInfoValue.class,
                    parentColumns = "id",
                    childColumns = "id_thumbs_direction",
                    onDelete = NO_ACTION
            ),
                @ForeignKey(
                    entity = ExerciseAbstractInfoValue.class,
                    parentColumns = "id",
                    childColumns = "id_separate_sides",
                    onDelete = NO_ACTION)})
public class ExerciseAbstract{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_exerciseabs")
    private int id;

    @ColumnInfo(name = "id_muscle")
    private int id_muscle;

    @ColumnInfo(name = "id_operation")
    private int id_operation;

    @ColumnInfo(name = "id_nickname")
    private Integer id_nickname;

    @ColumnInfo(name = "id_load_type")
    private int id_load_type;

    @ColumnInfo(name = "id_position")
    private int id_position;

    @ColumnInfo(name = "id_angle")
    private int id_angle;

    @ColumnInfo(name = "id_grip_width")
    private int id_grip_width;

    @ColumnInfo(name = "id_thumbs_direction")
    private int id_thumbs_direction;

    @ColumnInfo(name = "id_separate_sides")
    private int id_separate_sides;

    @Ignore
    String muscle, operation, nickname, load_type,
            position, grip_width, thumbs_direction ,separate_sides, angle;


    // ------------Constructors ---------------
    //For new ExerciseAbs
    @Ignore
    public ExerciseAbstract(int id_muscle, int id_operation, Integer id_nickname, int id_load_type,
                            int id_position, int id_angle, int id_grip_width, int id_thumbs_direction,
                            int id_separate_sides) {
        this.id = 0;
        this.id_muscle = id_muscle;
        this.id_operation = id_operation;
        this.id_nickname = id_nickname;
        this.id_load_type = id_load_type;
        this.id_position = id_position;
        this.id_angle = id_angle;
        this.id_grip_width = id_grip_width;
        this.id_thumbs_direction = id_thumbs_direction;
        this.id_separate_sides = id_separate_sides;

    }

    //For existing ExerciseAbs
    public ExerciseAbstract(int id, int id_muscle, int id_operation, Integer id_nickname, int id_load_type,
                            int id_position, int id_angle, int id_grip_width, int id_thumbs_direction,
                            int id_separate_sides) {
        this.id = id;
        this.id_muscle = id_muscle;
        this.id_operation = id_operation;
        this.id_nickname = id_nickname;
        this.id_load_type = id_load_type;
        this.id_position = id_position;
        this.id_angle = id_angle;
        this.id_grip_width = id_grip_width;
        this.id_thumbs_direction = id_thumbs_direction;
        this.id_separate_sides = id_separate_sides;

    }

    public ExerciseAbstract(){

    }


    //---------------------Getters & Setters---------------------


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_muscle() {
        return id_muscle;
    }

    public void setId_muscle(int id_muscle) {
        this.id_muscle = id_muscle;
    }

    public int getId_operation() {
        return id_operation;
    }

    public void setId_operation(int id_operation) {
        this.id_operation = id_operation;
    }

    public Integer getId_nickname() {
        return id_nickname;
    }

    public void setId_nickname(Integer id_nickname) {
        this.id_nickname = id_nickname;
    }

    public int getId_load_type() {
        return id_load_type;
    }

    public void setId_load_type(int id_load_type) {
        this.id_load_type = id_load_type;
    }

    public int getId_position() {
        return id_position;
    }

    public void setId_position(int id_position) {
        this.id_position = id_position;
    }

    public int getId_angle() {
        return id_angle;
    }

    public void setId_angle(int id_angle) {
        this.id_angle = id_angle;
    }

    public int getId_grip_width() {
        return id_grip_width;
    }

    public void setId_grip_width(int id_grip_width) {
        this.id_grip_width = id_grip_width;
    }

    public int getId_thumbs_direction() {
        return id_thumbs_direction;
    }

    public void setId_thumbs_direction(int id_thumbs_direction) {
        this.id_thumbs_direction = id_thumbs_direction;
    }

    public int getId_separate_sides() {
        return id_separate_sides;
    }

    public void setId_separate_sides(int id_separate_sides) {
        this.id_separate_sides = id_separate_sides;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLoad_type() {
        return load_type;
    }

    public void setLoad_type(String load_type) {
        this.load_type = load_type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGrip_width() {
        return grip_width;
    }

    public void setGrip_width(String grip_width) {
        this.grip_width = grip_width;
    }

    public String getThumbs_direction() {
        return thumbs_direction;
    }

    public void setThumbs_direction(String thumbs_direction) {
        this.thumbs_direction = thumbs_direction;
    }

    public String getSeparate_sides() {
        return separate_sides;
    }

    public void setSeparate_sides(String separate_sides) {
        this.separate_sides = separate_sides;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }


    // ----------------------------- FUNCTIONS ---------------------------------
    public String generateExerciseAbstractName(){
        String name = "";
        if (this.nickname == null || this.nickname.isEmpty()){
            name = this.muscle + " " + this.operation;
        }
        else{
            name = this.nickname + " " + this.operation;
        }
        return name.strip();
    }

    //https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
    public static Comparator<ExerciseAbstract> MuscleGroupComparator = new Comparator<ExerciseAbstract>() {

        public int compare(ExerciseAbstract ea1, ExerciseAbstract ea2) {
            boolean result;
            result = (ea1.getId_muscle() == ea2.getId_muscle());
            result = result && (ea1.getId_operation() == ea2.getId_operation());
            result = result && (ea1.getId_nickname() == ea2.getId_nickname());
            result = result && (ea1.getId_load_type() == ea2.getId_load_type());
            result = result && (ea1.getId_position() == ea2.getId_position());
            result = result && (ea1.getId_angle() == ea2.getId_angle());
            result = result && (ea1.getId_grip_width() == ea2.getId_grip_width());
            result = result && (ea1.getId_thumbs_direction() == ea2.getId_thumbs_direction());
            result = result && (ea1.getId_separate_sides() == ea2.getId_separate_sides());

            // if all above statements are true, return 1
            if (result)
                return 1;
            else
                return 0;
        }};

}

//OLD - before DB change

// ------------------------------------------ OLD --------------------------------------------------
//package com.example.tamirmishali.trainingmanager.ExerciseAbstract;
//
//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;

//
//import java.util.Comparator;
//
//@Entity(tableName = "exerciseabs_table")
//public class ExerciseAbstract{
//
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id_exerciseabs")
//    private int id;
//
//    @ColumnInfo(name ="name")
//    private String name;
//
//    @ColumnInfo(name = "description")
//    private String description;
//
//    @ColumnInfo(name = "muscleGroup")
//    private String muscleGroup;
//
//
//    // ------------Constructors ---------------
//    //For new ExerciseAbs
//    public ExerciseAbstract(String name, String description, String mussleGroup) {
//        this.id = 0;
//        this.name = name;
//        this.description = description;
//        this.muscleGroup = mussleGroup;
//    }
//
//    //For existing ExerciseAbs
//    public ExerciseAbstract(int id, String name, String description, String mussleGroup) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.muscleGroup = mussleGroup;
//    }
//
//    public ExerciseAbstract(){
//
//    }
//
//    //---------------------Getters & Setters---------------------
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getMuscleGroup() {
//        return muscleGroup;
//    }
//
//    public void setMuscleGroup(String muscleGroup) {
//        this.muscleGroup = muscleGroup;
//    }
//
//
//    //https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
//    public static Comparator<ExerciseAbstract> MuscleGroupComparator = new Comparator<ExerciseAbstract>() {
//
//        public int compare(ExerciseAbstract ea1, ExerciseAbstract ea2) {
//            String StudentName1 = ea1.getMuscleGroup().toUpperCase();
//            String StudentName2 = ea2.getMuscleGroup().toUpperCase();
//
//            //ascending order
//            return StudentName1.compareTo(StudentName2);
//
//            //descending order
//            //return StudentName2.compareTo(StudentName1);
//        }};
//
//
//
//
//}
