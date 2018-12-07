package com.example.tamirmishali.trainingmanager;

import java.util.ArrayList;


public class Workout {
    private String m_name; //=routineDate_Char(A/B/C)
    ArrayList<Exercise> exercises;
}


class Exercise{
    private String m_name;
    private String m_description;
    private String m_mussleGroup;
    int[] reps = new int[5]; //maximum 5 sets
}
