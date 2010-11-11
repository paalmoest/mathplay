/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mathplay;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Stian
 */
public class randomChallenge {

    private ChallengeBean chall = new ChallengeBean();
    String[] types = {"Addition", "Subtraction", "Multiplication", "Division"};

    public randomChallenge(){}

    public ChallengeBean makeChallenge(){

        int randomNumber = (int)(types.length*Math.random());
        String challType = types[randomNumber];
        System.out.println(challType);
        Random randomGenerator = new Random();
        int rand1 = randomGenerator.nextInt(100);
        int rand2 = randomGenerator.nextInt(100);
        double random1 = rand1;
        double random2 = rand2;

       
        if(challType.equals("Addition")){       
            chall.setType(challType);
            String challText = "Hva er "+random1+" + "+random2+" ?";
            chall.setText(challText);
            double challCorrect = random1 + random2;
            chall.setCorrect(challCorrect);
            if(random1 <=10 && random2 <= 10)chall.setDifficulty(1);
            else if(random1 <= 50 && random2 <= 50)chall.setDifficulty(2);
            else if(random1 <= 100 && random2 <= 100)chall.setDifficulty(3);
            System.out.println("Legger til + oppgave");

        }
        else if(challType.equals("Subtraction")){
            chall.setType(challType);
            String challText = "Hva er "+random1+" - "+random2+" ?";
            chall.setText(challText);
            double challCorrect = random1 - random2;
            chall.setCorrect(challCorrect);
            if(random1 <=10 && random2 <= 10)chall.setDifficulty(1);
            else if(random1 <= 50 && random2 <= 50)chall.setDifficulty(2);
            else if(random1 <= 100 && random2 <= 100)chall.setDifficulty(3);
            System.out.println("Legger til - oppgave");
        }
        else if(challType.equals("Multiplication")){
            chall.setType(challType);   
            String challText = "Hva er "+random1+" * "+random2+" ?";
            chall.setText(challText);
            double challCorrect = random1 * random2;
            chall.setCorrect(challCorrect);
            if(random1 <=10 && random2 <= 10)chall.setDifficulty(1);
            else if(random1 <=50 && random2 <= 50 )chall.setDifficulty(2);
            else if(random1 <= 100 && random2 <= 100)chall.setDifficulty(3);
            System.out.println("Legger til * oppgave");
        }
        else if(challType.equals("Division")){
            while(random1%random2 != 0){
                int rand3 = randomGenerator.nextInt(100);
                int rand4 = randomGenerator.nextInt(100);
                random1 = rand3;
                random2 = rand4;
            }
            chall.setType(challType);
            String challText = "Hva er "+random1+" / "+random2+" ?";
            chall.setText(challText);
            double challCorrect = random1 / random2;
            chall.setCorrect(challCorrect);
            if(random1 <=10 && random2 <= 10)chall.setDifficulty(1);
            else if(random1 <= 50 && random2 <= 50)chall.setDifficulty(2);
            else if(random1 <= 100 && random2 <= 100)
            chall.setDifficulty(3);
            System.out.println("Legger til / oppgave");
        }
        else{
            System.out.println("fail");
        }
        
        return chall;

    }
}
