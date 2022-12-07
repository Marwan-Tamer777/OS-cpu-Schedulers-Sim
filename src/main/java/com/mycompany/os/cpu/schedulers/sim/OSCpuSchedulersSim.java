/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.os.cpu.schedulers.sim;

import java.util.Scanner;

/**
 *
 * Os Section AI-4
 * 20200508
 * 20200318
 * 20200474
 * 20200043
 * 20200172
 */
public class OSCpuSchedulersSim {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Scanner scan = new Scanner(System.in);
        
        while(true){
            System.out.print("Please enter your desired Schudeling algortihm\n "
                    + "1- SFJ (Preemitve shortest job first).\n 2- RR (Round Robin).\n 3- PS (Priority Scheduling).\n 4- AG\n 5- exit.\n"); 
            String choice;
            choice = scan.nextLine();
            choice = choice.toLowerCase();
            
            switch (choice) {
            case "sfj":
            case "1":  
              SFJ sfj = new SFJ();
              sfj.run();
              sfj.printResult();
              break;
            case "rr":
            case "2": 
              RR rr = new RR();
              rr.run();
              rr.printResult();
              break;
             case "ps":
             case "3": 
              PS ps = new PS();
              ps.run();
              ps.printResult();
              break;
             case "ag":
             case "4": 
              AG ag = new AG();
              ag.run();
              ag.printResult();
              break;
             case "exit":
             case "5": 
              System.out.println("Closing the Program");
              return;
            default:
              System.out.println("Wrong Input");
          }
        }
    }
}
