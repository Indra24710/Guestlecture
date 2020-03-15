package com.example.guestlec;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
public class UnitTesting {
    @Test
    public void checkEmail(){

        Unittest t=new Unittest();
        String input1="name@email.com";
        boolean output;
        output=t.checkEmailForValidity(input1);
        assertEquals(true,output);
        String input2="name@email";
        output=t.checkEmailForValidity(input2);
        assertEquals(false,output);
        String input3="@email.com";
        output=t.checkEmailForValidity(input3);
        assertEquals(false,output);
        String input4="name@email..com";
        output=t.checkEmailForValidity(input4);
        assertEquals(false,output);
        String input5="Name1234@email.com";
        output=t.checkEmailForValidity(input5);
        assertEquals(true,output);
    }
    @Test
    public void passwordCheck(){
        Unittest t=new Unittest();
        String input1="Abcdefgh123*&";
        boolean output;
        output=t.checkPasswordValidity(input1);
        assertEquals(true,output);
        String input2="Abcd()12345&*";
        output=t.checkPasswordValidity(input2);
        assertEquals(true,output);
        String input3="abcd";
        output=t.checkPasswordValidity(input3);
        assertEquals(false,output);
        String input4="123456789";
        output=t.checkPasswordValidity(input4);
        assertEquals(false,output);
        String input5="abcd1234567";
        output=t.checkPasswordValidity(input5);
        assertEquals(false,output);
        String input6="abcd 1234567";
        output=t.checkPasswordValidity(input6);
        assertEquals(false,output);
    }
}
