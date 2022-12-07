/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.os.cpu.schedulers.sim;

import static java.lang.Math.ceil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author 10
 */
public class AG {
    List<Process> processes;
    List<Process> activePs;
    List<Process> finishedPs;
    List<String> schedule;
    List<List<Integer>> quantums;
    int processesNum;
    int contextSwitch;
    int currentTime = 0;
    String temp;
    
    public AG(){
        this.setUpSchedule();
    };
    
    public void setUpSchedule(){
        processes = new ArrayList<Process>();
        activePs = new ArrayList<Process>();
        finishedPs = new ArrayList<Process>();
        schedule = new ArrayList<String>();
        quantums = new ArrayList<List<Integer>>();
        Scanner scan = new Scanner(System.in);
        int tempI;
        
        System.out.println("Please Enter Number Of Processes: ");
        tempI = scan.nextInt();
        processesNum = tempI;
        
        contextSwitch = 0;
        
        //Testing
        /*processes.add(new Process("P1",0,17,4,7));
        processes.add(new Process("P2",2,6,7,9));
        processes.add(new Process("P3",5,11,3,4));
        processes.add(new Process("P4",15,4,6,6));*/
        for(int i=0;i<processesNum;i++){
            Process p = new Process();
            p.enterQuantumProcess();
            processes.add(p);
        }
    }
    
    //Each 1 time unit we check on the avail Jobs to see if a shortest one is available.
    public void run(){
        temp = "-1";
        Process active = null;
        List<Integer> arr = null;
        
        while(!(finishedPs.size() == processes.size())){
            int low,high;
            
            for(int i=0;i<processes.size();i++){
                if(processes.get(i).arrivalTime<=currentTime){
                    if(!activePs.contains(processes.get(i)) && !finishedPs.contains(processes.get(i))){
                        activePs.add(processes.get(i));
                    }
                }
            }
            arr = new ArrayList<Integer>();
            for(int i=0;i<processes.size();i++){
                arr.add(processes.get(i).quantum);
            }
            quantums.add(arr);
            
            if(activePs.isEmpty()){currentTime+= 1;continue;}
            //choose the current process to get processed in the fist iteration only.
            if(temp.equals("-1")){
                active = activePs.get(0);
            }
            low = (int) ceil(0.25*active.quantum);
            high = (int) ceil(0.50*active.quantum);
            
            
            //If the previous Process is not the same as the current shortest one, then we will
            //puhs the new one in the schedule and add context switch overhead.
            if(!temp.equals(active.name)){
                //to not add context switching on first job
                if(!temp.equals("-1")){currentTime+=contextSwitch;}
                schedule.add(active.name);
                temp = activePs.get(0).name;
            }
            
            
            //Update time and choose next active process
            if(active.remainingTime<=low){
                currentTime+= active.remainingTime;
                active.remainingTime = 0;
                active.exitTime = currentTime;
                finishedPs.add(active);
                activePs.remove(active);
                active.quantum =0;
                if(activePs.isEmpty()){break;}
                active = activePs.get(0);
                continue;
            } else {
                currentTime+= low;
                active.remainingTime -= low;
            }
            
            Collections.sort(activePs, new SortByPiority());
            
            if(!active.name.equals(activePs.get(0).name)){
                active.quantum += (int)ceil((active.quantum - low)/2);
                active = activePs.get(0);
                
            } else {
                if(active.remainingTime<=(high - low)){
                    currentTime+= active.remainingTime;
                    active.remainingTime = 0;
                    active.exitTime = currentTime;
                    finishedPs.add(active);
                    activePs.remove(active);
                    active.quantum =0;
                    active = activePs.get(0);
                    continue;
                } else {
                    currentTime+= (high - low);
                    active.remainingTime -= (high - low);
                }
                Collections.sort(activePs, new SortByRemainingTime());
                active.quantum += (int)ceil((active.quantum - high));
                active = activePs.get(0);
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
        System.out.println("Name : Turn Around : Waiting");
        for(int i = 0;i<finishedPs.size();i++){
            finishedPs.get(i).calcTimes();
            totalTurnAround+=finishedPs.get(i).turnAroundTime;
            totalWaiting+=finishedPs.get(i).waitingTime;
            
            System.out.println(finishedPs.get(i).name + " " + finishedPs.get(i).turnAroundTime + " " + finishedPs.get(i).waitingTime);
        }
        
        System.out.println("Average Turn Around Time = " + (totalTurnAround/finishedPs.size()) +
                " Average Waiting Time = " + (totalWaiting/finishedPs.size()));
        
        for(int x=0;x<quantums.size();x++){
            System.out.print("Qunatum for " + (x+1) + " Rotations: ");
            for(int y=0;y<quantums.get(x).size();y++){
                System.out.print( " " +quantums.get(x).get(y));
            }
            System.out.println();
        }
    };
    
}
