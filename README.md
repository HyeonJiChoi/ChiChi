## 1. Introduce of API
![ezgif com-video-to-gif (1)](https://user-images.githubusercontent.com/44078732/84570660-e2017700-adc9-11ea-86d2-3ff58e2af074.gif)

- We make API the source of problems by connecting them through fragments.
- This has been make API to 2 multiple-choice, 3 multiple-choice, and CDT. 
- Examples of this problem are current date(3 multiple-choice), season(2 multiple-choice), calculation problem(3 multiple-choice), and CDT.
- This problem are exchanged at 20 sec, CDT is 1 min

## 2. Description of files
In the APIS, you can get source and resource.
In java, TestScreen.java is MainActivity. 

And InspectorBlankFragment.java(FirstFragment), InspectorTestProblemList2.java(2 multiple-choice), InspectorTestProblemList3.java(3 multiple-choice), "InspectorTestProblemCDT.java(CDT)" are Fragment in TestScreen.java.

DrawCDTCanvas.java, GetCDTScore.java are java file for InspectorTestProblemCDT.java

## 3. How to use
If you want use all codes, you should change package
```javascript
package yourpackage.package.package;
```
### 2 multiple-choice
You want to use 2 multiple-choice, you should Input codes in onChangeFragment() of TestScreen.java.
And you input first choice, second choice, answer in show_2problem().
For example, You want to make season problem.
```javascript
public void onChangeFragment() {
        int answer=0;
        timer.cancel();
        if (testNumber < total_testNumber) {
            System.out.println(testNumber);
            String todayDate = getDateString();
            String[] wordDate = todayDate.split("-");
            switch (testNumber) {                
                case 0: // make 2 multiple-choice. You change this part.
                    fragmentProblemQuestionText.setText("지금 연도와 월은\n언제인가요?"); //change problem text
                    fragmentProblemQuestionQNumber.setText(Integer.toString(realCount + 1)); //change problem text
                    String[] seasons = new String[2];
                    seasons[0]="봄";
                    seasons[1]="여름";
                    show_2problem(seasons[0], seasons[1], answer);
                    break;
```
### 3 multiple-choice
You want to use 3 multiple-choice, you should Input codes in onChangeFragment() of TestScreen.java.
And you input first choice, second choice, third choice, answer in show_3problem().
For example, You want to make calculation problem.
```javascript
public void onChangeFragment() {
        int answer=0;
        timer.cancel();
        if (testNumber < total_testNumber) {
            System.out.println(testNumber);
            String todayDate = getDateString();
            String[] wordDate = todayDate.split("-");
            switch (testNumber) {                
                case 0: // make 3 multiple-choice. You change this part.
                    fragmentProblemQuestionText.setText("1 + 1 = ?"); //change problem text
                    fragmentProblemQuestionQNumber.setText(Integer.toString(realCount + 1)); //change problem text
                    answer = 2;
                    String[] numbers = new String[2];
                    numbers[0]="0";
                    numbers[1]="1";
                    numbers[2]="2";
                    show_2problem(numbers[0], numbers[1], numbers[2], answer);
                    break;
```
### CDT
You want to use CDT, you should Input codes in onChangeFragment() of TestScreen.java.
And you input set hour in show_CDTProblem.
For example, You want to make calculation problem.
```javascript
public void onChangeFragment() {
        int answer=0;
        timer.cancel();
        if (testNumber < total_testNumber) {
            System.out.println(testNumber);
            String todayDate = getDateString();
            String[] wordDate = todayDate.split("-");
            switch (testNumber) {                
                case 0: // make 3 multiple-choice. You change this part.
                    int hour = 2;
                    int minutes = 0;
                    fragmentProblemQuestionText.setText(hour + "시 정각을 그려주세요");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(realCount + 1));
                    show_CDTProblem(hour);
                    break;
```

And If you want CDT seperately Activity, 
- You should input DrawCDTCanvas in xml.
- You should use get_score() in newGetCDTScore.java.

```xml
    <package yourpackage.package.package.DrawCDTCanvas
        android:id="@+id/InspectorTestProblemCDTCanvas"
        android:layout_marginTop="10dp"
        android:layout_width="360dp"
        android:layout_height="360dp"
        android:layout_centerHorizontal="true"
        android:background="@drawable/othershadow"/>
```
If you want to get score when click button, do it.
```javascript
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCDTScore newGetCDTScore = new GetCDTScore(drawCDTCanvas, answer) ;
                cdt_score = newGetCDTScore.get_score();

            }
        });
```
## 4. CDT Algorithm
![image](https://user-images.githubusercontent.com/44078732/84652063-f10d3400-af45-11ea-9bce-f3d31397d147.png)

- In the CDT implementation, if the problem is to draw the 12 o'clock hour, +2 points if there are 2 lines.
- If the length of the minute hand is greater than total/4 in all parts, +1 point.
- When entering the light gray area, the second and minute are each +1 point. When entering the red area, which is the dark gray area, +1 point is added.
