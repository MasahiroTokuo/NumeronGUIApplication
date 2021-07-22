package numeronmode.player1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class ButtonPanel extends JPanel{
    private RulesPanel rule;
    private int[] count, answer;
    private JButton newGame, reset, attack;
    private ArrayList<JButton> actionButtonList, numberButtonList;
    private ArrayList<JLabel> inputLabelList, resultLabelList;
    private ArrayList<Integer> numbers;
    private JFrame nf;

    public ButtonPanel(ArrayList<JLabel>inputLabelList, ArrayList<JLabel> resultLabelList, JFrame nf)throws Exception{
        count = new int[] {0};
        answer = makeAnswer();
        this.inputLabelList = inputLabelList;
        this.resultLabelList = resultLabelList;
        this.nf = nf;
        actionButtonList = new ArrayList<JButton>();
        numberButtonList = new ArrayList<JButton>();
        numbers = new ArrayList<Integer>();
        setLayout(new GridLayout(3,1));
        rule = new RulesPanel();
        setActionButton();
        setNumberButton();
        addPanels();
    }

    private void setActionButton()throws Exception{
        newGame = new JButton("NEW GAME");
        newGame.setForeground(new Color(255,60,60));
        newGame.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 40));
        actionButtonList.add(newGame);

        reset = new JButton("RESET");
        reset.setForeground(Color.blue);
        reset.setBackground(new Color(200,255,200));
        reset.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 45));
        reset.setBorderPainted(false);
        reset.setOpaque(true);
        actionButtonList.add(reset);

        attack = new JButton("ATTACK!!");
        attack.setForeground(new Color(33,33,33));
        attack.setBackground(new Color(200,255,255));
        attack.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 40));
        attack.setBorderPainted(false);
        attack.setOpaque(false);
        actionButtonList.add(attack);
        attack.setEnabled(false);


        newGame.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent evt){
                    NewGameDialog ngd = new NewGameDialog(nf);
                }
            }
        );

        reset.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent evt){
                    inputLabelList.get(count[0]).setText("【" + Integer.toString(count[0]+1) + "回目】  ");
                    resetButton();
                }
            }
        );

        attack.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent evt){
                    int[] attackNumber = new int[4];
                    for(int i = 0; i < attackNumber.length; i++){
                        attackNumber[i] = numbers.get(i);
                    }
                    int[] eatBite = discrimination(answer, attackNumber);
                    resultLabelList.get(count[0]).setText(" →   " + eatBite[0] + "EAT " + eatBite[1] + "BITE");
                    
                    if(eatBite[0] == 4){
                        GameClearDialog gcd = new GameClearDialog(nf);
                        disableButtons();
                    }else{
                        resetButton();
                        count[0]++;
                    }
                    if(count[0] == 8){
                        GameOverDialog god = new GameOverDialog(nf);
                        disableButtons();
                    }
                }
            }
        );
    }

    private void setNumberButton()throws Exception{
        for(int i = 0; i < 10; i++){  //数字ボタンの生成
            numberButtonList.add(new JButton(Integer.toString(i)));
            JButton b = numberButtonList.get(i);
            if( i % 2 == 0){
                b.setBackground(new Color(200,255,255));
            }else{
                b.setBackground(new Color(170,255,255));
            }
            b.setForeground(new Color(33,33,33));
            b.setBorderPainted(false);
            b.setOpaque(true);
            b.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 60));
        
            b.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent evt){  //数字ボタンクリック時のアクション
                        JButton bn = (JButton)evt.getSource();
                        String number = bn.getText();
                        inputLabelList.get(count[0]).setText(inputLabelList.get(count[0]).getText() + number);
                        numbers.add(Integer.parseInt(number));
                        if(numbers.size() == 4){
                            for(JButton b : numberButtonList){
                                b.setEnabled(false);
                                b.setOpaque(false);
                            }
                            attack.setEnabled(true);
                            attack.setOpaque(true);
                        }else{
                            bn.setEnabled(false);
                            bn.setOpaque(false);
                        }
                    }
                }
            );
        }
    }

    private void addPanels(){
        JPanel b1to3 = addButtons(numberButtonList.get(1),numberButtonList.get(2),numberButtonList.get(3));
        JPanel b4to6 = addButtons(numberButtonList.get(4),numberButtonList.get(5),numberButtonList.get(6));
        JPanel b7to9 = addButtons(numberButtonList.get(7),numberButtonList.get(8),numberButtonList.get(9));
        JPanel b0reset = addButtons(numberButtonList.get(0),reset);

        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(3,1));
        p1.add(newGame); p1.add(b1to3); p1.add(b4to6);
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(3,1));
        p2.add(b7to9); p2.add(b0reset); p2.add(attack);

        add(rule); add(p1); add(p2);
    }

    private JPanel addButtons(JButton b1, JButton b2, JButton b3){
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1,3));
        p.add(b1); p.add(b2); p.add(b3);
        return p;
    }
    private JPanel addButtons(JButton b1, JButton b2){
        JPanel p = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        p.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0d;
        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(b1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0d;
        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(b2, gbc);

        p.add(b1); p.add(b2);
        return p;
    }

    private void resetButton(){     //ボタンを初期状態に戻すメソッド
        for(JButton b : numberButtonList){
            b.setEnabled(true);
            b.setOpaque(true);
        }
        attack.setEnabled(false);
        attack.setOpaque(false);
        reset.setEnabled(true);
        reset.setOpaque(true);
        numbers.clear();
    }

    private void disableButtons(){
        for(JButton b : numberButtonList){
            b.setEnabled(false);
            b.setOpaque(false);
        }
        attack.setEnabled(false);
        attack.setOpaque(false);
        reset.setEnabled(false);
        reset.setOpaque(false);
    }

    private int[] makeAnswer(){ //4桁のランダムな数字を生成するメソッド
		ArrayList<Integer> intList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            intList.add(i);
        }
        int[] ans = new int[4];
	    for(int i = 0; i < ans.length; i++){
            int random = new java.util.Random().nextInt(intList.size());
	        ans[i] = intList.get(random);
            intList.remove(random);
	    }
	    return ans;
	}

    private int[] discrimination(int[] ans, int[] attackNumber){    //EATとBITEを数えて表示するメソッド
	    int eat = 0;
	    int bite = 0;
	    for(int i = 0; i < 4; i++){
	        for(int j = 0; j < 4; j++){
	            if(attackNumber[i] == ans[j]){
	                bite++;
	            }
	        }
	    }
	    for(int i = 0; i < 4; i++){
	        if(attackNumber[i] == ans[i]){
	            eat++;
	        }
	    }
        int[] eatBite = {eat, (bite - eat)};
	    return eatBite;
    }

    class NewGameDialog extends JDialog implements ActionListener{   //new gameボタンを押した時に表示するダイアログ
        JFrame owner;
        NewGameDialog(JFrame owner){
            super(owner);
            this.owner = owner;
            setTitle("NEW GAME");
            setVisible(true);
            Insets insets = getInsets();
            setVisible(false);
            setSize(300 + insets.right + insets.left, 150 + insets.top + insets.bottom);
            setLayout(null);

            JLabel confirmation = new JLabel("新しいゲームを始めますか？");
            confirmation.setBounds(50, 20, 200, 70);
            confirmation.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
            confirmation.setHorizontalAlignment(JLabel.CENTER);
            JButton okButton = new JButton("OK");
            okButton.setBounds(100, 80, 100, 30);
            okButton.addActionListener(this);
            add(confirmation); add(okButton);

            
            setResizable(false);
            setModal(true);
            setLocationRelativeTo(null);
            setVisible(true);
        }
        public void actionPerformed(ActionEvent evt){
            dispose();
            owner.dispose();
            try{
                numeronmode.SelectModeDialog smd = new numeronmode.SelectModeDialog();
            }catch(Exception e){
                System.out.println("想定外のエラーです");
                e.getMessage();
                e.printStackTrace();
            }
        }
    }

    class GameClearDialog extends JDialog{  //ゲームクリア時に表示するダイアログ
        GameClearDialog(JFrame owner){
            super(owner);
            setTitle("GAME CLEAR!!");
            setVisible(true);
            Insets insets = getInsets();
            setVisible(false);
            setSize(300 + insets.right + insets.left, 150 + insets.top + insets.bottom);
            setLayout(null);

            JLabel confirmation = new JLabel("<html>おめでとうございます！<br>" + (count[0]+1) + "回目で正解しました！<html>");
            confirmation.setBounds(50, 30, 200, 70);
            confirmation.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
            confirmation.setHorizontalAlignment(JLabel.CENTER);
            add(confirmation);

            
            setResizable(false);
            setModal(true);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    class GameOverDialog extends JDialog{   //8回以内に正解できなかった時に表示するダイアログ
        GameOverDialog(JFrame owner){
            super(owner);
            setTitle("GAME OVER");
            setVisible(true);
            Insets insets = getInsets();
            setVisible(false);
            setSize(300 + insets.right + insets.left, 150 + insets.top + insets.bottom);
            setLayout(null);

            String strAnswer = "";
            for(int i : answer){
                strAnswer += Integer.toString(i);
            }
            JLabel confirmation = new JLabel("設定ナンバーは" + strAnswer + "でした");
            confirmation.setBounds(50, 20, 200, 70);
            confirmation.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
            confirmation.setHorizontalAlignment(JLabel.CENTER);
            add(confirmation);

            
            setResizable(false);
            setModal(true);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
}