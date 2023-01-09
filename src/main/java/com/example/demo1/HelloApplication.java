package com.example.demo1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;

class setNull extends Thread{

    setNull(long time1,long time2,HelloApplication app,int clickTimes){
        startTime = time1;
        endTime =time2;
         application = app;
         this.clickTimes = clickTimes;
    }
    int clickTimes;
    long startTime;
    long endTime;
    HelloApplication application;
    @Override
    public void run() {
        try {
            Thread.sleep(400);
            Platform.runLater(() -> {
                Alert a = new Alert(CONFIRMATION,"成功完成，用时："+String.valueOf((endTime-startTime)/1000)+"秒"+" 点击次数："+String
                        .valueOf(clickTimes));
                        a.setTitle("确认");
                        a.setHeaderText("确认");
                a.showAndWait();
                application.restartGame();
            }
            );

        }catch (InterruptedException e) {
            System.out.println("Thread " + " interrupted.");
        }
        super.run();
    }

    @Override
    public synchronized void start() {
        super.start();
    }
}

public class HelloApplication extends Application {

    long startTime;
    Label[][] lab = new Label[4][4];
    MenuBar menuBar = new MenuBar();
    List<Integer> list = new ArrayList<Integer>(9);
    Random random = new Random();
    int lastimageNum = 16;

    Label temp_lab1;
    Label temp_lab2;
    Scene scene = new Scene(new Group(),600,625, Color.WHITE);
    int clicks = 0;
    public void setLab(){
        Random random = new Random();

        for(int i=0;i<8;i++){
            int key = random.nextInt(2099)+1;
            list.add(key);
            list.add(key);
        }

        for (int i = 0; i < 4; i++) {
            for (int ii = 0; ii < 4; ii++) {
                String s=(String.valueOf(random.nextInt(20)));
                lab[i][ii] = new Label();
                lab[i][ii].setPrefSize(150, 150);
                lab[i][ii].setAlignment(Pos.CENTER);

                Label temp  = lab[i][ii];
                temp.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e){
                        if(temp.getGraphic()!=null){
                            return;
                        }
                        String s = (String) temp.getUserData();
                        ImageView img = new ImageView();//TODO 修改图片位置
//                        System.out.println(s);
                        Image image=new Image(getClass().getResourceAsStream(s));
                        img.setImage(image);
                        temp.setGraphic(img);
                        lastimageNum--;
                        clicks++;
                        if(lastimageNum%2==1){
                            if(lastimageNum<14&&!temp_lab1.getUserData().equals(temp_lab2.getUserData())){
                                temp_lab1.setGraphic(null);
                                temp_lab2.setGraphic(null);
                                lastimageNum+=2;
                            }
//                            System.out.println(lastimageNum);
                            temp_lab1 = temp;
                        }
                        else{
                            temp_lab2 = temp;
                            if(lastimageNum==0){
                                setNull setTime = new setNull(startTime,System.currentTimeMillis(),HelloApplication.this,clicks);
                                setTime.start();
                            }
                        }
                    }});
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new BorderPane();


        stage.setResizable(false);
        setLab();


        menuBar.setLayoutX(0);
        menuBar.setLayoutY(0);
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        Menu fileMenu = new Menu("游戏");
        MenuItem newMenuItem = new MenuItem("新游戏");
        newMenuItem.setOnAction((ActionEvent t)->{
            restartGame();
        });
        fileMenu.getItems().add(newMenuItem);
        menuBar.getMenus().add(fileMenu);


        Line line1 = new Line();
        line1.setStartX(0.0f);
        line1.setStartY(175.0f);
        line1.setEndX(600.0f);
        line1.setEndY(175.0f);

        Line line2 = new Line();
        line2.setStartX(0.0f);
        line2.setStartY(325.0f);
        line2.setEndX(600.0f);
        line2.setEndY(325.0f);

        Line line3 = new Line();
        line3.setStartX(0.0f);
        line3.setStartY(325.0f+150.0f);
        line3.setEndX(600.0f);
        line3.setEndY(325.0f+150.0f);

        Line line4 = new Line();
        line4.setStartX(150.0f);
        line4.setStartY(0+25);
        line4.setEndX(150.0f);
        line4.setEndY(600.0f+25);

        Line line5 = new Line();
        line5.setStartX(300);
        line5.setStartY(0+25);
        line5.setEndX(300);
        line5.setEndY(600.0f+25);

        Line line6 = new Line();
        line6.setStartX(450);
        line6.setStartY(0+25);
        line6.setEndX(450);
        line6.setEndY(600.0f+25);

        root.getChildren().add(line1);
        root.getChildren().add(line2);
        root.getChildren().add(line3);
        root.getChildren().add(line4);
        root.getChildren().add(line5);
        root.getChildren().add(line6);

        ((Group) scene.getRoot()).getChildren().add(root);

        restartGame();
        stage.setTitle("记忆游戏");
        stage.setScene(scene);
        stage.show();
    }

    public void restartGame(){
        clicks = 0;
        GridPane root = new GridPane();

        lastimageNum = 16;
        for(int i=0;i<16;i++){
            int key = random.nextInt(2099)+1;
            while(key<835&&key>818){
                key = random.nextInt(2099)+1;
            }
            list.set(i,key);
            i++;
            list.set(i,key);
        }

        Collections.shuffle(list);

        for (int i = 0; i < 4; i++) {
            for (int ii = 0; ii < 4; ii++) {
                lab[i][ii].setGraphic(null);
                String s = getPath(i*4+ii);
                System.out.println(s);
                lab[i][ii].setUserData(s);
                root.add(lab[i][ii],i,ii);
            }
        }

        root.setPadding(new Insets(25, 0, 0, 0));
        ((Group) scene.getRoot()).getChildren().add(root);
        ((Group)scene.getRoot()).getChildren().remove(menuBar);
        ((Group)scene.getRoot()).getChildren().add(menuBar);

        startTime = System.currentTimeMillis();
    }

    String getPath(int i){
        int key = list.get(i);
        String num = String.valueOf(key);
        String s;
        if(key<884||key>1000){
            s = "/images/png-";
        }
        else s = "/images/PNG-";
        for(int j=0;j<4-num.length();j++){
            s+="0";
        }
        s+=num;
        s+=".png";
        return s;
    }

    public static void main(String[] args) {
        launch();
    }
}