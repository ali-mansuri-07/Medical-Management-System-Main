package com.nrifintech.medicalmanagementsystem.utility.enums;

public enum Rating {
    VALUE_1(1),
    VALUE_2(2),
    VALUE_3(3),
    VALUE_4(4),
    VALUE_5(5);


    private final int value;

    Rating(int value) {
        this.value = value;
    }
}
// public int getStoredValue() {
//             return ordinal()+1;
//         }

// public enum Rating {
//     VALUE_1(1),
//     VALUE_2(2),
//     VALUE_3(3);

//     private final int value;

//     Rating(int value) {
//         this.value = value;
//     }

//     public int getValue() {
//         return value;
//     }
// }
