package com.beecloud.main;

import com.beecloud.monitor.DbMonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DbMonitor4BeeCloud {
	public static void main(String[] args){
		new DbMonitor4BeeCloud().run(new String[]{"DbConfig.yml"});
	}
	
	public void run(String[] args){
		String config = args[0];
		DbMonitor dbMonitor = new DbMonitor(config);
		while (true){
			BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
			String s=null;
			try {
				s=bf.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if("START".equals(s.toUpperCase())){
				try {
					dbMonitor.startMonitor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if("SHOW".equals(s.toUpperCase())){
				dbMonitor.endMonitor();
			}
		}
	}
	
	
//    private void showUsage() {
//		System.out.println("Usage:");
//		System.out.println("use \"start\" To Start Monitor");
//		System.out.println("use \"show\" To Show the Changes");
//        System.out.println("If any issue,please contact me:hong.lin@beecloud.com");
//    }

}
