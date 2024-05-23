package com.nrifintech.medicalmanagementsystem.mapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
@Component
public class SlotTimeMapper {
    private Map<Integer, String> slotToTimeMap = new HashMap<>();
    private Map<String, Integer> timeToSlotMap = new HashMap<>();

    public SlotTimeMapper() {
        populateMappings();
    }

    private void populateMappings() {
        
        slotToTimeMap.put(1, "10:15");
        slotToTimeMap.put(2, "10:30");
        slotToTimeMap.put(3, "10:45");
        slotToTimeMap.put(4, "11:00");
        slotToTimeMap.put(5, "11:15");
        slotToTimeMap.put(6, "11:30");
        slotToTimeMap.put(7, "11:45");
        slotToTimeMap.put(8, "12:00");
        slotToTimeMap.put(9, "12:15");
        slotToTimeMap.put(10, "12:30");
        slotToTimeMap.put(11, "12:45");
        slotToTimeMap.put(12, "13:00");
        slotToTimeMap.put(13, "14:15");
        slotToTimeMap.put(14, "14:30");
        slotToTimeMap.put(15, "14:45");
        slotToTimeMap.put(16, "16:00");
        slotToTimeMap.put(17, "16:15");
        slotToTimeMap.put(18, "16:30");
        slotToTimeMap.put(19, "16:45");
        slotToTimeMap.put(20, "16:00");
        slotToTimeMap.put(21, "16:15");
        slotToTimeMap.put(22, "16:30");
        slotToTimeMap.put(23, "16:45");
        slotToTimeMap.put(24, "17:00");

        for (Map.Entry<Integer, String> entry : slotToTimeMap.entrySet()) {
            timeToSlotMap.put(entry.getValue(), entry.getKey());
        }
    }
    public String getTimeFromSlot(int slotNumber) {
        return slotToTimeMap.get(slotNumber);
    }

    public int getSlotFromTime(String time) {
        Integer slot = timeToSlotMap.get(time);
        return slot != null ? slot : -1; 
    }

   
}
