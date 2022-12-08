/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.os.cpu.schedulers.sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author 10
 */
public class RR {
    List<Process> processes;
    List<Process> activePs;
    List<Process> finishedPs;
    List<String> schedule;
    List<Integer> scheduleTimes;
    int processesNum;
    int contextSwitch;
    int quantum;
    int currentTime = 0;
    Process tempP;
    String temp;
    
    public RR(){
        this.setUpSchedule();
    };
    
    public void setUpSchedule(){
        processes = new ArrayList<Process>();
        activePs = new ArrayList<Process>();
        finishedPs = new ArrayList<Process>();
        schedule = new ArrayList<String>();
        scheduleTimes = new ArrayList<Integer>();
        Scanner scan = new Scanner(System.in);
        int tempI;
        
        System.out.println("Please Enter Number Of Processes: ");
        tempI = scan.nextInt();
        processesNum = tempI;
        
        System.out.println("Please Enter Context Switching Overhead: ");
        tempI = scan.nextInt();
        contextSwitch = tempI;
        
        System.out.println("Please Enter Quantum: ");
        tempI = scan.nextInt();
        quantum = tempI;
        
        //Teting
        /*processes.add(new Process("P1",0,24));
        processes.add(new Process("P2",0,3));
        processes.add(new Process("P3",0,3));*/
        for(int i=0;i<processesNum;i++){
            Process p = new Process();
            p.enterNormalProcess();
            processes.add(p);
        }
    }
    
    //Each 1 time unit we check on the avail Jobs to see if a shortest one is available.
    public void run(){
        temp = "-1";
        while(!(activePs.isEmpty() && processes.isEmpty())){
            
            for(int i=0;i<processes.size();i++){
                if(processes.get(i).arrivalTime<=currentTime){
                    activePs.add(processes.get(i));
                    processes.remove(i);
                    i--;
                }
            }
            
            if(activePs.isEmpty()){currentTime+= quantum;continue;}
            
            schedule.add(activePs.get(0).name);
            scheduleTimes.add(currentTime);
             //If the previous Process is not the same as the current shortest one, then we will add context switch overhead.
            if(!temp.equals(activePs.get(0).name)){
                //to not add context switching on first job
                if(!temp.equals("-1")){currentTime+=contextSwitch;}
                temp = activePs.get(0).name;
            }
            
            //if reamining time less that qunatium then deduce reamining time not quantum
            if(activePs.get(0).remainingTime<quantum){
                currentTime+=activePs.get(0).remainingTime;
                activePs.get(0).remainingTime =0;
            } else {
                currentTime+= quantum;
                activePs.get(0).remainingTime -= quantum;
            }
            
            if(activePs.get(0).remainingTime <= 0){
                activePs.get(0).exitTime = currentTime;
                finishedPs.add(activePs.get(0));
                activePs.remove(0);
            } else {
                tempP = activePs.get(0);
                activePs.remove(0);
                activePs.add(tempP);
            }
            
        }
        
    };
    
    public void printResult(){
        int totalTurnAround = 0;
        int totalWaiting = 0;
        for(int i = 0;i<schedule.size();i++){
            System.out.print(schedule.get(i) + " ");
        }
        System.out.println();
        for(int i = 0;i<scheduleTimes.size();i++){
            System.out.print(scheduleTimes.get(i) + " ");
        }
        System.out.println();
        System.out.println("Name : Turn Around : Waiting");
        for(int i = 0;i<finishedPs.size();i++){
            finishedPs.get(i).calcTimes();
            totalTurnAround+=finishedPs.get(i).turnAroundTime;
            totalWaiting+=finishedPs.get(i).waitingTime;
            
            System.out.println(finishedPs.get(i).name + " " + finishedPs.get(i).turnAroundTime + " " + finishedPs.get(i).waitingTime);
        }
        
        System.out.println("Average Turn Around Time = " + (totalTurnAround/finishedPs.size()) +
                " Average Waiting Time = " + (totalWaiting/finishedPs.size()));
    };

}
