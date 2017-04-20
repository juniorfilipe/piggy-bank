package com.example.filipe.piggybank.Fragments;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filipe.piggybank.R;
import com.example.filipe.piggybank.Utils.Services;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoinsFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "CoinsFragment";


    final double VALUE_COIN_1 = 2.0;
    final double VALUE_COIN_2 = 1.0;
    final double VALUE_COIN_3 = 0.50;
    final double VALUE_COIN_4 = 0.20;
    final double VALUE_COIN_5 = 0.10;
    final double VALUE_COIN_6 = 0.05;
    final double VALUE_COIN_7 = 0.02;
    final double VALUE_COIN_8 = 0.01;

    private String DEFAULT_VALUE = "0"; //value to be set at the beguinning of the app or reset
    private TextView totalValueTextView;
    private EditText numberCoins1, numberCoins2, numberCoins3, numberCoins4, numberCoins5, numberCoins6, numberCoins7, numberCoins8;
    private Button incButtonCoin1,incButtonCoin2,incButtonCoin3,incButtonCoin4,incButtonCoin5,incButtonCoin6,incButtonCoin7,incButtonCoin8;
    private Button decButtonCoin1,decButtonCoin2,decButtonCoin3,decButtonCoin4,decButtonCoin5,decButtonCoin6,decButtonCoin7,decButtonCoin8;
    private Button resetButton, loadButton, saveButton;

    private List<Double> l_values = new ArrayList<>();
    ArrayList<Integer> l_absoluteFr = new ArrayList<>();

    private SimpleArrayMap<Integer,Integer> mMapOfIndexToCoinTextView = new SimpleArrayMap<>();


    private String fileName = "piggyBankRecords.txt";
    private File root = Environment.getExternalStorageDirectory();
    private File dir = new File(root.getAbsolutePath() + "/piggybank");
    private File file = new File(dir,fileName);

    private DecimalFormat df = new DecimalFormat("#.00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coins_layout, container, false);
        initWidgetComponents(view);
        setDefaultValuesToNumberOfCoinsEditText();
        setListOfValueOfCoins();
        setmMapOfIndexToCoinTextView();



//        setOnClickList();



        setButtonListeners();
//

//        List<EditText> listWithEditText = getListWithEditTexts();
//        //setListOfDecrementButtons();
//
//        //Services reader = new Services();




        return view;
    }

    @Override
    public void onClick(View v) {


        String idAsString = v.getResources().getResourceEntryName(v.getId());
        String buttonName = "null";
        String func = idAsString.substring(0,3);
        if(func.equalsIgnoreCase("inc"))
        {
             buttonName = "incrementButton";

        }else if(func.equalsIgnoreCase("dec"))
        {
            buttonName = "decrementButton";
        }
        double totalValueUpdate = 0;
        switch (buttonName) {
            case "incrementButton":
                Toast.makeText(getContext(),func,Toast.LENGTH_SHORT).show();
                System.out.println("totalValueUpdate BEFORE update!!!\n" + String.valueOf(totalValueUpdate));
                Log.d(TAG,String.valueOf(totalValueUpdate));
                totalValueUpdate = Double.valueOf(df.format(updateTotalValue(v,numberCoins1)));
                System.out.println("totalValueUpdate AFTER update!!!\n" + String.valueOf(totalValueUpdate));
                Log.d(TAG,String.valueOf(totalValueUpdate));
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
                break;
            case "decrementButton":
                Toast.makeText(getContext(),func,Toast.LENGTH_SHORT).show();
                totalValueUpdate = Double.valueOf(df.format(updateTotalValue(v,numberCoins1)));
                break;
            case "null":
                Toast.makeText(getContext(),func,Toast.LENGTH_SHORT).show();
            default:
                Toast.makeText(getContext(),"UNKNOWN BUTTON",Toast.LENGTH_SHORT).show();

        }

//        double totalValueUpdate;
//        totalValueUpdate = Double.valueOf(df.format(updateTotalValue(v,numberCoins1)));
//        totalValueTextView.setText(String.valueOf(totalValueUpdate));
    }


    public double updateTotalValue(View v, EditText numberOfCoinsEditText)
    {

        String idAsString,nameTypeOfButton,numberOfCoins;


        idAsString = v.getResources().getResourceEntryName(v.getId());
        nameTypeOfButton = idAsString.substring(0,3);//"inc" or "dec" of increment and decrement
        numberOfCoins = numberOfCoinsEditText.getText().toString();
        System.out.println("NUMBER OF COINS: " + numberOfCoins);
        //index--;
        //guarda o valor das moedas antes de o alterar
        int totalNumberOfCoinsBefore;

        if(numberOfCoins.equalsIgnoreCase(" "))
        {
            totalNumberOfCoinsBefore = 0;
            System.out.println("TOTAL NUMBER OF COINS BEFORE: " + totalNumberOfCoinsBefore);
        }
        else
        {
            totalNumberOfCoinsBefore = Integer.valueOf(numberOfCoins);
            System.out.println("TOTAL NUMBER OF COINS BEFORE: " + totalNumberOfCoinsBefore);
        }

        //e agora muda o campo e actualiza o valor de acordo com o botao
        int totalNumberOfCoinsAfter = 0;
        if(nameTypeOfButton.equalsIgnoreCase("inc")) {
            System.out.println("INCREMENT");
            totalNumberOfCoinsAfter = (int) addCoinToEditText(numberOfCoinsEditText);
            numberOfCoinsEditText.setText(String.valueOf(totalNumberOfCoinsAfter));
        }
        if(nameTypeOfButton.equalsIgnoreCase("dec"))
        {
            System.out.println("DECREMENT");
            totalNumberOfCoinsAfter = (int) takeCoinFromEditText(numberOfCoinsEditText);
            numberOfCoinsEditText.setText(String.valueOf(totalNumberOfCoinsAfter));
        }

        //parte final do update

        System.out.println("TOTAL NUMBER OF COINS AFTER: " + totalNumberOfCoinsAfter);

        int diff =
                totalNumberOfCoinsAfter - totalNumberOfCoinsBefore;
        System.out.println("DIFF: " + diff);

        System.out.println(String.format("ViewID: " + v.getId()));
        int correspondingValueIndex = getCorrespondingEditTextValueIndex(v);
        System.out.println("CORRESPONDING INDEX:  " + correspondingValueIndex);
        int index = getMapOfEditTextIndex().get(numberOfCoinsEditText);
        double diffOfValue = diff * l_values.get(index);
        double totalValueBefore = Double.valueOf(totalValueTextView.getText().toString());
        double totalValueUpdate = totalValueBefore + diffOfValue;

//        //Predition prototype BEGIN
//        Prediction p = new Prediction();
//        int day1 = 1;
//        int day2 = 13;
//        double totalPredictionValue = p.getTotalPreditionOnTargetDay(totalValueUpdate,day1,day2);
//        String totalPrediction = String.valueOf(df.format(totalPredictionValue));
//        predictionTextView.setText(totalPrediction);
//        //Prototype END

        return totalValueUpdate;
    }

    private void setOnClickList()
    {
        incButtonCoin1.setOnClickListener(this);
        incButtonCoin2.setOnClickListener(this);
        incButtonCoin3.setOnClickListener(this);
        incButtonCoin4.setOnClickListener(this);
        incButtonCoin5.setOnClickListener(this);
        incButtonCoin6.setOnClickListener(this);
        incButtonCoin7.setOnClickListener(this);
        incButtonCoin8.setOnClickListener(this);

        decButtonCoin1.setOnClickListener(this);
        decButtonCoin2.setOnClickListener(this);
        decButtonCoin3.setOnClickListener(this);
        decButtonCoin4.setOnClickListener(this);
        decButtonCoin5.setOnClickListener(this);
        decButtonCoin6.setOnClickListener(this);
        decButtonCoin7.setOnClickListener(this);
        decButtonCoin8.setOnClickListener(this);




    }


    private SimpleArrayMap<EditText,Integer> getMapOfEditTextIndex()
    {
        SimpleArrayMap<EditText,Integer> mapOfEditTextIndex = new SimpleArrayMap<>();
        int i = 0;
        for(EditText t : getListWithEditTexts())
        {
            mapOfEditTextIndex.put(t,i);
            i++;
        }

        return mapOfEditTextIndex;
    }



    /**
     * Convert a text in an EditText field to a string
     * @param editText the number of coins
     * @return a string with the representation of number of coins in editText
     */
    private String convertEditTextToString(EditText editText)
    {
        String s;
        s = editText.getText().toString();

        return s;
    }

    /**
     * Returns the value of the total as a string
     * @return the string representation of the Total var/value
     */
    private String getTotalAsString(TextView totalValue)
    {
        String string;
        string = totalValue.getText().toString();

        return string;
    }






    private List<EditText> getListWithEditTexts()
    {
        List<EditText> list =
                Arrays.asList(numberCoins1,numberCoins2,numberCoins3,numberCoins4,
                        numberCoins5,numberCoins6,numberCoins7,numberCoins8);
        return list;
    }

    private List<Button> setListOfIncrementButtons()
    {
        List<Button> l_incButtons =
                Arrays.asList(incButtonCoin1,incButtonCoin2,incButtonCoin3,incButtonCoin4,
                        incButtonCoin4,incButtonCoin5,incButtonCoin6,incButtonCoin7,incButtonCoin8);
        return l_incButtons;
    }


    public int getCorrespondingEditTextValueIndex(View view) {
        int index = 0;

        switch (view.getId()) {
            case R.id.coinText1:
                index = 0;
                break;

            case R.id.coinText2:
                index = 1;
                break;

            case R.id.coinText3:
                index = 2;
                break;

            case R.id.coinText4:
                index = 3;
                break;

            case R.id.coinText5:
                index = 4;
                break;

            case R.id.coinText6:
                index = 5;
                break;

            case R.id.coinText7:
                index = 6;
                break;

            case R.id.coinText8:
                index = 7;
                break;
        }
        return index;
    }

    private void setButtonListeners()
    {

        incButtonCoin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double totalValueUpdate;
                totalValueUpdate = Double.valueOf(df.format(updateTotalValue(v,numberCoins1)));
                totalValueTextView.setText(String.valueOf(totalValueUpdate));

            }
        });

        incButtonCoin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = Double.valueOf(df.format(updateTotalValue(v,numberCoins2)));
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });

        incButtonCoin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = Double.valueOf(df.format(updateTotalValue(v,numberCoins3)));
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });

        incButtonCoin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = Double.valueOf(df.format(updateTotalValue(v,numberCoins4)));
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });
        incButtonCoin5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = Double.valueOf(df.format(updateTotalValue(v,numberCoins5)));
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });
        incButtonCoin6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = Double.valueOf(df.format(updateTotalValue(v,numberCoins6)));
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });
        incButtonCoin7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = Double.valueOf(df.format(updateTotalValue(v,numberCoins7)));
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });
        incButtonCoin8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = Double.valueOf(df.format(updateTotalValue(v,numberCoins8)));
                totalValueTextView.setText(String.valueOf(totalValueUpdate));

            }
        });


        /////////////////////////////////////////////////////////////
        decButtonCoin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = updateTotalValue(v,numberCoins1);
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });

        decButtonCoin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = updateTotalValue(v,numberCoins2);
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });

        decButtonCoin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = updateTotalValue(v,numberCoins3);
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });

        decButtonCoin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = updateTotalValue(v,numberCoins4);
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });
        decButtonCoin5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = updateTotalValue(v,numberCoins5);
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });
        decButtonCoin6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = updateTotalValue(v,numberCoins6);
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });
        decButtonCoin7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = updateTotalValue(v,numberCoins7);
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });
        decButtonCoin8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalValueUpdate = updateTotalValue(v,numberCoins8);
                totalValueTextView.setText(String.valueOf(totalValueUpdate));
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefaultValuesToNumberOfCoinsEditText();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Services services = new Services(fileName);
                services.loadValuesFromFile(getListWithEditTexts());
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Services service = new Services();
                service.writeToSD(getListOfEditTextAsString(),v.getContext(), totalValueTextView);
            }
        });

    }


    //this method is buggy
    //este metodo talvez devesse ser void apenas para alterar o valor do total
    // e depois arranjar outro metodo para devolver
    public double addCoinToEditText(EditText numberOfCoinsEditText){
        double total;
        String numberOfCoinsText = numberOfCoinsEditText.getText().toString();
        System.out.println("NUMBER OF COINS TEXT:" + numberOfCoinsText);

        //se nao tiver nada para nao dar null tem que ficar a zero
        //ou entao fazer catch do NullPointerException
        if(numberOfCoinsText.equalsIgnoreCase(""))
        {
            numberOfCoinsEditText.setText(DEFAULT_VALUE);//total = 0
            total = Integer.valueOf(numberOfCoinsText);
        }
        else
        {
            total = Integer.valueOf(numberOfCoinsText);
            total++;
        }

        return total;
    }

    public double takeCoinFromEditText(EditText editText){
        double total;
        String numberOfCoins;

        numberOfCoins = editText.getText().toString();

        total = Integer.valueOf(numberOfCoins);
        total--;

        if(total < 0 )
        {
            total = 0;
        }
        return total;
    }


    private ArrayList<Integer> setListWithAbsoluteFrequencyOfCoins(List<EditText> list)
    {
        ArrayList<Integer> l_absoluteFr = new ArrayList<>();
        for(EditText e: list)
        {
            int totalCoin = Integer.parseInt(e.getText().toString());
            l_absoluteFr.add(totalCoin);
        }
        return l_absoluteFr;
    }

    private ArrayList<Integer> getListWithAbsoluteFrequencyOfCoins()
    {
        return l_absoluteFr;
    }

    private void setListOfValueOfCoins()
    {
        l_values = Arrays.asList(VALUE_COIN_1,VALUE_COIN_2,VALUE_COIN_3,VALUE_COIN_4,VALUE_COIN_5,VALUE_COIN_6,VALUE_COIN_7,VALUE_COIN_8);

    }
    private void initWidgetComponents(View view)
    {
        //initialize EditTexts
        numberCoins1 = (EditText) view.findViewById(R.id.coinText1);
        numberCoins2 = (EditText) view.findViewById(R.id.coinText2);
        numberCoins3 = (EditText) view.findViewById(R.id.coinText3);
        numberCoins4 = (EditText) view.findViewById(R.id.coinText4);
        numberCoins5 = (EditText) view.findViewById(R.id.coinText5);
        numberCoins6 = (EditText) view.findViewById(R.id.coinText6);
        numberCoins7 = (EditText) view.findViewById(R.id.coinText7);
        numberCoins8 = (EditText) view.findViewById(R.id.coinText8);

        //initialize button of addition
        incButtonCoin1 = (Button) view.findViewById(R.id.incButton1);
        incButtonCoin2 = (Button) view.findViewById(R.id.incButton2);
        incButtonCoin3 = (Button) view.findViewById(R.id.incButton3);
        incButtonCoin4 = (Button) view.findViewById(R.id.incButton4);
        incButtonCoin5 = (Button) view.findViewById(R.id.incButton5);
        incButtonCoin6 = (Button) view.findViewById(R.id.incButton6);
        incButtonCoin7 = (Button) view.findViewById(R.id.incButton7);
        incButtonCoin8 = (Button) view.findViewById(R.id.incButton8);

        //initialize buttons of subtraction
        decButtonCoin1 = (Button) view.findViewById(R.id.decButton1);
        decButtonCoin2 = (Button) view.findViewById(R.id.decButton2);
        decButtonCoin3 = (Button) view.findViewById(R.id.decButton3);
        decButtonCoin4 = (Button) view.findViewById(R.id.decButton4);
        decButtonCoin5 = (Button) view.findViewById(R.id.decButton5);
        decButtonCoin6 = (Button) view.findViewById(R.id.decButton6);
        decButtonCoin7 = (Button) view.findViewById(R.id.decButton7);
        decButtonCoin8 = (Button) view.findViewById(R.id.decButton8);

        //initialize TextViews
        totalValueTextView = (TextView) view.findViewById(R.id.totalTextView);

        //options buttons
        resetButton = (Button) view.findViewById(R.id.resetButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        loadButton = (Button) view.findViewById(R.id.loadButton);
    }

    public ArrayList<String> getListOfEditTextAsString()
    {
        ArrayList<String> listOfEditTextAsString = new ArrayList<>();

        String value;
        for(EditText e : getListWithEditTexts())
        {
            value = e.getText().toString();
            listOfEditTextAsString.add(value);
        }

        return listOfEditTextAsString;

    }

    private void setDefaultValuesToNumberOfCoinsEditText()
    {
        EditText editText;
        for(int i = 0; i< getListWithEditTexts().size(); i++)
        {
            editText = getListWithEditTexts().get(i);
            editText.setText(DEFAULT_VALUE);
        }


    }
    private void setmMapOfIndexToCoinTextView()
    {
        int i = 1;
        for(EditText editText : getListWithEditTexts())
        {
            mMapOfIndexToCoinTextView.put(editText.getId(),i);
            i++;
        }

    }



}
