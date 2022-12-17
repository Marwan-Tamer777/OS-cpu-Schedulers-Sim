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
    List<Process> NonSortedPs;
    List<Process> activePs;
    List<Process> finishedPs;
    List<String> schedule;
    List<Integer> scheduleTimes;
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
        NonSortedPs = new ArrayList<Process>();
        activePs = new ArrayList<Process>();
        finishedPs = new ArrayList<Process>();
        schedule = new ArrayList<String>();
        scheduleTimes = new ArrayList<Integer>();
        quantums = new ArrayList<List<Integer>>();
        Scanner scan = new Scanner(System.in);
        int tempI;
        
        System.out.println("Please Enter Number Of Processes: ");
        tempI = scan.nextInt();
        processesNum = tempI;
        
        contextSwitch = 0;
        
        //Testing
        processes.add(new Process("P1",0,17,4,7));
        processes.add(new Process("P2",2,6,7,9));
        processes.add(new Process("P3",5,11,3,4));
        processes.add(new Process("P4",15,4,6,6));
        /*for(int i=0;i<processesNum;i++){
            Process p = new Process();
            p.enterQuantumProcess();
            processes.add(p);
            NonSortedPs.add(p);
        }*/
    }
    
    //Each 1 time unit we check on the avail Jobs to see if a shortest one is available.
    public void run(){
        temp = "-1";
        Process active = null;
        while(!(finishedPs.size() == processes.size())){
            int low,high;
            RefreshActives();
            
            if(activePs.isEmpty()){currentTime+= 1;continue;}
            //choose the current process to get processed in the fist iteration only.
            if(temp.equals("-1")){
                active = NonSortedPs.get(0);
            }
            RecordState(active);
            low = (int) ceil(0.25*active.quantum);
            high = (int) ceil(0.50*active.quantum);
            
            
            //Update time and choose next active process
            //if PorcessTime retuns 1 means that the process have finished and moved to the finished list.
            
            //From 0 to 25%(low).
            if(ProcessTime(active,low)==1){
                if(activePs.isEmpty()){continue;}
                active = NonSortedPs.get(0);
                continue;
            } else {
                //From 25 to 50%(high). Choose next process using priority.
                RefreshActives();
                Collections.sort(activePs, new SortByPiority());
                if(!active.name.equals(activePs.get(0).name)){
                    active.quantum += (int)ceil((double)(active.quantum - low)/2);
                    PlaceAtEnd(active);
                    active = activePs.get(0);
                    continue;
                }
                
                
                if(ProcessTime(active,high-low)==1){
                    if(activePs.isEmpty()){continue;}
                    RefreshActives();
                    Collections.sort(activePs, new SortByPiority());
                    active = activePs.get(0);
                    continue;
                } else {
                    //From 50 to 100%(qunatum).  Choose next process using remaining time. if finished chooe firt come firt serve
                    RefreshActives();
                    Collections.sort(activePs, new SortByRemainingTime());
                    if(!active.name.equals(activePs.get(0).name)){
                        active.quantum += (int)ceil((double)(active.quantum - high));
                        PlaceAtEnd(active);
                        active = activePs.get(0);
                        continue;
                    }
                    
                    int availPreemtiveTime = active.quantum - high;
                    while(availPreemtiveTime>0){
                        RefreshActives();
                        Collections.sort(activePs, new SortByRemainingTime());
                        if(!active.name.equals(activePs.get(0).name)){
                            active.quantum += (int)ceil((active.quantum - high));
                            PlaceAtEnd(active);
                            active = activePs.get(0);
                            break;
                        } else {
                            if(ProcessTime(active,1)==1){
                                if(activePs.isEmpty()){break;}
                                active = NonSortedPs.get(0);
                                break;
                            }
                        }
                        availPreemtiveTime--;
                    }
                    if(activePs.isEmpty()){continue;}
                    if(active.name.equals(NonSortedPs.get(0).name)){continue;}
                    
                    active.quantum *=2;
                    PlaceAtEnd(active);
                    RefreshActives();
                    Collections.sort(activePs, new SortByRemainingTime());
                    active = activePs.get(0);
                }
            }
            
            //-----------------------------------------------
            /*
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
            }*/
          
            
        }
        
        RecordState(active);
        scheduleTimes.add(currentTime);
    };
    
    public void printResult(){
        int totalTurnAround = 0;
        int totalWaiting = 0;
        System.out.print("  ");
        for(int i = 0;i<schedule.size();i++){
            System.out.print(schedule.get(i) + "  ");
        }
        System.out.println();
        for(int i = 0;i<scheduleTimes.size();i++){
            System.out.print(String.format("%02d",scheduleTimes.get(i)) + "  ");
        }
        System.out.println();
        System.out.println("Name : Turn Around : Waiting");
        for(int i = 0;i<finishedPs.size();i++){
            finishedPs.get(i).calcTimes();
            totalTurnAround+=finishedPs.get(i).turnAroundTime;
            totalWaiting+=finishedPs.get(i).waitingTime;
            
            System.out.println(finishedPs.get(i).name + " " + finishedPs.get(i).turnAroundTime + " " + finishedPs.get(i).waitingTime);
        }
        
        System.out.println("Average Turn Around Time = " + ((double)totalTurnAround/finishedPs.size()) +
                " Average Waiting Time = " + ((double)totalWaiting/finishedPs.size()));
        
        for(int x=0;x<quantums.size();x++){
            System.out.print("Qunatum for " + (x+1) + " Rotations: ");
            for(int y=0;y<quantums.get(x).size();y++){
                System.out.print( " " +quantums.get(x).get(y));
            }
            System.out.println();
        }
    };
    
    public int ProcessTime(Process p,int time){
        if(p.remainingTime<=time){
            currentTime+= p.remainingTime;
            p.remainingTime = 0;
            p.exitTime = currentTime;
            finishedPs.add(p);
            activePs.remove(p);
            NonSortedPs.remove(p);
            p.quantum =0;
            return 1;
        } else {
            currentTime+= time;
             p.remainingTime -= time;
         }
        return 0;
    };
    
    public void RefreshActives(){
        for(int i=0;i<processes.size();i++){
            if(processes.get(i).arrivalTime<=currentTime){
                if(!activePs.contains(processes.get(i)) && !finishedPs.contains(processes.get(i))){
                    activePs.add(processes.get(i));
                    NonSortedPs.add(processes.get(i));
                }
            }
        }
    };
    
    public void PlaceAtEnd(Process p){
        NonSortedPs.remove(p);
        NonSortedPs.add(p);
    };
    
    public void RecordState(Process active){
        //If the previous Process is not the same as the current shortest one, then we will
        //puss the new one in the schedule and add context switch overhead.
        if(!temp.equals(active.name)){
            //to not add context switching on first job
            if(!temp.equals("-1")){currentTime+=contextSwitch;}
            schedule.add(active.name);
            scheduleTimes.add(currentTime);
            //if(!activePs.isEmpty()){temp = activePs.get(0).name;}
            temp = active.name;
        }
            
        List<Integer> arr = null;
        arr = new ArrayList<Integer>();
        for(int i=0;i<processes.size();i++){
            arr.add(processes.get(i).quantum);
        }
        quantums.add(arr);
    };
}
