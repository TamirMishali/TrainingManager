package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.tamirmishali.trainingmanager.Database.ExerciseAbstractRepository;

import java.util.List;

public class ExerciseAbstractViewModel extends AndroidViewModel {
    private ExerciseAbstractRepository repository;
    private LiveData<List<ExerciseAbstract>> allExerciseAbstracts;

    // new
//    private LiveData<List<ExerciseAbstractInfoValue>> allInfoValues;
//    private LiveData<List<ExerciseAbstractOperation>> allOperations;
//    private LiveData<List<ExerciseAbstractNickname>> allNicknames;
    private MediatorLiveData<Long> exerciseAbstractId = new MediatorLiveData<>();


    public ExerciseAbstractViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseAbstractRepository(application);
//        allRoutines = repository.getAllRoutines();
        allExerciseAbstracts = repository.getAllExerciseAbstracts();
//        allInfoValues = repository.getAllExerciseAbstractInfoValues();
//        allOperations = repository.getAllExerciseAbstractOperations();
//        allNicknames = repository.getAllExerciseAbstractNicknames();

    }
    public void insert(ExerciseAbstract exerciseabstract){
        repository.insert(ExerciseAbstractStringsToIds(exerciseabstract));
    }
    public void update(ExerciseAbstract exerciseabstract){
        repository.update(exerciseabstract);
    }
    public void delete(ExerciseAbstract exerciseabstract){
        repository.delete(exerciseabstract);
    }
    public void deleteAllExerciseAbstracts(){
        repository.deleteAllExerciseAbstracts();
    }
    public LiveData<List<ExerciseAbstract>> getAllExerciseAbstracts(){
        return allExerciseAbstracts;
    }
    public LiveData<List<ExerciseAbstract>> getExerciseAbstractsForWorkout(int id){ return repository.getExerciseAbstractsForWorkout(id); }


    public ExerciseAbstract getExerciseAbsFromId(int exerciseAbsId){
        return ExerciseAbstractIdsToStrings(repository.getExerciseAbsFromId(exerciseAbsId));}


    // new
//    public LiveData<List<ExerciseAbstractInfoValue>> getAllExerciseAbstractInfoValues(){ return allInfoValues;}
//    public LiveData<List<ExerciseAbstractOperation>> getAllExerciseAbstractOperations(){ return allOperations;}
//    public LiveData<List<ExerciseAbstractNickname>> getAllExerciseAbstractNicknames(){ return allNicknames;}
    public int getExerciseAbstractInfoValueId(String value){return repository.getExerciseAbstractInfoValueId(value);}
    public int getExerciseAbstractOperationId(String id_muscle, String operation){return repository.getExerciseAbstractOperationId(id_muscle, operation);}
    public int getExerciseAbstractNicknameId(String nickname){return repository.getExerciseAbstractNicknameId(nickname);}
    private String getExerciseAbstractInfoValueValue(int id_value){return repository.getExerciseAbstractInfoValueValue(id_value);}
    private String getExerciseAbstractOperationOperation(int id_operation){return repository.getExerciseAbstractOperationOperation(id_operation);}
    private String getExerciseAbstractNicknameNickname(int id_nickname){return repository.getExerciseAbstractNicknameNickname(id_nickname);}
    public List<String> getExerciseAbstractInfoValueValueByHeader(String info_header_name){return repository.getExerciseAbstractInfoValueValueByHeader(info_header_name);}
    public List<String> getExerciseAbstractOperationByMuscleId(int id_muscle){return repository.getExerciseAbstractOperationByMuscleId(id_muscle);}
    public List<String> getExerciseAbstractNicknameByOperationId(int id_operation){return repository.getExerciseAbstractNicknameByOperationId(id_operation);}
    public void insertOperation(ExerciseAbstractOperation exerciseAbstractOperation) {
        repository.insert(exerciseAbstractOperation);
//        long id = repository.insert(exerciseAbstractOperation);
    }
    //public void getOperation(int id) {repository.getExerciseAbstractOperation(id);}
    public void insertNickname(ExerciseAbstractNickname exerciseAbstractNickname) {repository.insert(exerciseAbstractNickname);}

    // Thanks gptChat that answered the next questions:
    // 1. how can i get the id of a recent inserted row in room database
    // 2. how can i get this id when using a ViewModel, and not the Dao directly?
    // 3. im using a "Repository" class to manage the Dao from the ViewModel. how can i access the MediatorLiveData object that way?
    public LiveData<Long> getInsertedExerciseAbstractId() { return repository.getInsertedExerciseAbstractId(); }

/*    public List<ExerciseAbstractInfo> getExerciseAbstractsInfo(int id){ return repository.getExerciseAbstractsInfo(id); }
    public List<ExerciseAbstractInfoValue> getExerciseAbstractsInfoValue(int id){ return repository.getExerciseAbstractsInfoValue(id); }
    public List<ExerciseAbstractOperation> getExerciseAbstractsOperations(int id){return repository.getExerciseAbstractsOperations(id);}
    public List<ExerciseAbstractNickname> getExerciseAbstractsOperationNicknames(int id){return repository.getExerciseAbstractsOperationNicknames(id);}*/


    // Todo: Conclusions: LiveData is not meant to be iterated, only used for observation.
    public ExerciseAbstract ExerciseAbstractIdsToStrings(@NonNull ExerciseAbstract exerciseAbstract){

        // Start with mandatory id's: Muscle and Operation:
        exerciseAbstract.setMuscle(getExerciseAbstractInfoValueValue(exerciseAbstract.getId_muscle()));
        exerciseAbstract.setOperation(getExerciseAbstractOperationOperation(exerciseAbstract.getId_operation()));

        // Get the rest if exists:
        // If nickname id is not null or invalid (<1), set its string to be right.
        if (!(exerciseAbstract.getId_nickname() == 0)){
            exerciseAbstract.setNickname(getExerciseAbstractNicknameNickname(exerciseAbstract.getId_nickname()));
        }
        // If load_type id is not null or invalid (<1), set its string to be right.
        if (!(exerciseAbstract.getId_load_type() == 0)){
            exerciseAbstract.setLoad_type(getExerciseAbstractInfoValueValue(exerciseAbstract.getId_load_type()));
        }
        // If position id is not null or invalid (<1), set its string to be right.
        if (!(exerciseAbstract.getId_position() == 0)){
            exerciseAbstract.setPosition(getExerciseAbstractInfoValueValue(exerciseAbstract.getId_position()));
        }
        // If angle id is not null or invalid (<1), set its string to be right.
        if (!(exerciseAbstract.getId_angle() == 0)){
            exerciseAbstract.setAngle(getExerciseAbstractInfoValueValue(exerciseAbstract.getId_angle()));
        }
        // If grip_width id is not null or invalid (<1), set its string to be right.
        if (!(exerciseAbstract.getId_grip_width() == 0)){
            exerciseAbstract.setGrip_width(getExerciseAbstractInfoValueValue(exerciseAbstract.getId_grip_width()));
        }
        // If thumbs_direction id is not null or invalid (<1), set its string to be right.
        if (!(exerciseAbstract.getId_thumbs_direction() == 0)){
            exerciseAbstract.setThumbs_direction(getExerciseAbstractInfoValueValue(exerciseAbstract.getId_thumbs_direction()));
        }
        // If separate_sides id is not null or invalid (<1), set its string to be right.
        if (!(exerciseAbstract.getId_separate_sides() == 0)){
            exerciseAbstract.setSeparate_sides(getExerciseAbstractInfoValueValue(exerciseAbstract.getId_separate_sides()));
        }

        return exerciseAbstract;
    }
    public ExerciseAbstract ExerciseAbstractStringsToIds(@NonNull ExerciseAbstract exerciseAbstract){
        // If muscle string is not null or empty, set its id to be right.
        if (exerciseAbstract.getMuscle() == null || exerciseAbstract.getMuscle().isEmpty()){
            return null;
        }
        else {
            exerciseAbstract.setId_muscle(getExerciseAbstractInfoValueId(exerciseAbstract.getMuscle()));
        }

        // If operation string is not null or empty, set its id to be right.
        if (exerciseAbstract.getOperation() == null || exerciseAbstract.getOperation().isEmpty()){
            return null;
        }
        else {
            int opId = getExerciseAbstractOperationId(String.valueOf(exerciseAbstract.getId_muscle()),
                                                                    exerciseAbstract.getOperation());
            exerciseAbstract.setId_operation(opId);
        }
        // The rest of the fields are not mandatory, so just check if they are not null or empty,
        // and if so, find the right id and set it to the right field:

        // If nickname string is not null or empty, set its id to be right.
//        if (!(exerciseAbstract.getNickname() == null || exerciseAbstract.getNickname().isEmpty())){
        if (!(exerciseAbstract.getNickname().isEmpty())){
            exerciseAbstract.setId_nickname(getExerciseAbstractNicknameId(exerciseAbstract.getNickname()));
        }
        // If load_type string is not null or empty, set its id to be right.
//        if (!(exerciseAbstract.getLoad_type() == null || exerciseAbstract.getLoad_type().isEmpty())){
        if (!(exerciseAbstract.getLoad_type() == null)){
            exerciseAbstract.setId_load_type(getExerciseAbstractInfoValueId(exerciseAbstract.getLoad_type()));
        }
        // If position string is not null or empty, set its id to be right.
//        if (!(exerciseAbstract.getPosition() == null || exerciseAbstract.getPosition().isEmpty())){
        if (!(exerciseAbstract.getPosition() == null)){
            exerciseAbstract.setId_position(getExerciseAbstractInfoValueId(exerciseAbstract.getPosition()));
        }
        // If angle string is not null or empty, set its id to be right.
//        if (!(exerciseAbstract.getAngle() == null || exerciseAbstract.getAngle().isEmpty())){
        if (!(exerciseAbstract.getAngle() == null)){
            exerciseAbstract.setId_angle(getExerciseAbstractInfoValueId(exerciseAbstract.getAngle()));
        }
        // If grip_width string is not null or empty, set its id to be right.
//        if (!(exerciseAbstract.getGrip_width() == null || exerciseAbstract.getGrip_width().isEmpty())){
        if (!(exerciseAbstract.getGrip_width() == null)){
                exerciseAbstract.setId_grip_width(getExerciseAbstractInfoValueId(exerciseAbstract.getGrip_width()));
        }
        // If thumbs_direction string is not null or empty, set its id to be right.
//        if (!(exerciseAbstract.getThumbs_direction() == null || exerciseAbstract.getThumbs_direction().isEmpty())){
        if (!(exerciseAbstract.getThumbs_direction() == null)){
            exerciseAbstract.setId_thumbs_direction(getExerciseAbstractInfoValueId(exerciseAbstract.getThumbs_direction()));
        }
        // If separate_sides string is not null or empty, set its id to be right.
//        if (!(exerciseAbstract.getSeparate_sides() == null || exerciseAbstract.getSeparate_sides().isEmpty())){
        if (!(exerciseAbstract.getSeparate_sides() == null)){
            exerciseAbstract.setId_separate_sides(getExerciseAbstractInfoValueId(exerciseAbstract.getSeparate_sides()));
        }

        return exerciseAbstract;
    }
}
