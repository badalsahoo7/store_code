
package com.example.vivify_technocrats;

public class ModelClass {

    String Question;
    String qA;
    String qB;
    String qC;
    String qD;
    String ans;


    public ModelClass(){

    }


    public ModelClass( String question, String qA, String qB, String qC, String qD,String ans) {

        Question = question;
        this.qA = qA;
        this.qB = qB;
        this.qC = qC;
        this.qD = qD;
        this.ans = ans;
    }


    public void setQuestion(String question) {
        Question = question;
    }
    public String getQuestion() {
        return Question;
    }




    public void setqA(String qA) {
        this.qA = qA;
    }
    public String getqA() {
        return qA;
    }




    public void setqB(String qB) {
        this.qB = qB;
    }
    public String getqB() {
        return qB;
    }




    public void setqC(String qC) {
        this.qC = qC;
    }
    public String getqC() {
        return qC;
    }




    public void setqD(String qD) {
        this.qD = qD;
    }
    public String getqD() {
        return qD;
    }





    public void setAns(String ans) {
        this.ans = ans;
    }
    public String getAns() {
        return ans;
    }



}

