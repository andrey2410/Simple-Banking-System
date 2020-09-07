package banking.logic;
import java.util.Random;
import banking.domain.*;
public class Banking {
    private Cards cards;
    public Banking(){
        this.cards=new Cards();
    }
    public CreditCard createAccount(){
        Random random=new Random();
        String iin="400000"; //Issuer Identification Number
        String can=""; //Customer Account Number
        for(int i=1;i<=9;i++){
            can+=""+random.nextInt(10);
        }
        String cardNumber=iin+can;
        String checkDigit=checkDigit(cardNumber); //Luhn algorithm
        cardNumber+=checkDigit;
        String pin="";
        for(int i=1;i<=4;i++){
            pin+=""+random.nextInt(10);
        }
        CreditCard card=new CreditCard(cardNumber, pin);
        this.cards.addCard(card);
        return card;
    }
    public void createAccount(String cardNumber, String pin){
        CreditCard card=new CreditCard(cardNumber, pin);
        this.cards.addCard(card);
    }
    public CreditCard logIntoAccount(String cardNumber, String pin){
        for(CreditCard card:this.cards.getCards()){
            if(card.getCardNumber().equals(cardNumber)){
                if(card.getPin().equals(pin)){
                    return card;
                }
            }
        }
        return null;
    }
    public CreditCard logIntoAccount(String cardNumber){
        for(CreditCard card:this.cards.getCards()){
            if(card.getCardNumber().equals(cardNumber)){
                return card;
            }
        }
        return null;
    }
    private String checkDigit(String cardNumber){
        int sum=0;
        for(int i=0;i<=14;i++){
            int num=Integer.valueOf(cardNumber.substring(i,i+1));
            if(i%2==0){
                num=num*2;
            }
            if(num>9){
                num-=9;
            }
            sum+=num;

        }
        int checkDigit=0;
        for(int i=0;i<=9;i++){
            if((i+sum)%10==0){
                checkDigit=i;
            }
        }
        return ""+checkDigit;
    }
    public void clearCards(){
        this.cards.clearCards();
    }
}
