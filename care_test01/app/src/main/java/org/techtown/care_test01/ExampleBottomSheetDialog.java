package org.techtown.care_test01;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Locale;

public class ExampleBottomSheetDialog extends BottomSheetDialogFragment {


    TextView bot_name,bot_address,bot_tell;
    // 초기변수 설정
    private View view;
    // 인터페이스 변수
    private BottomSheetListener mListener;
    // 바텀시트 숨기기 버튼
    private Button btn_hide_bt_sheet;
    private static final String TAG = "Cannot invoke method length() on null object";
    String 도착지x ,도착지y,출발지x,출발지y,출발지주소;
    private Geocoder geocoder;
    List<Address> addressList = null;

    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        mListener = (BottomSheetListener) getContext();

        btn_hide_bt_sheet =view.findViewById(R.id.btn_hide_bt_sheet);
        bot_name=view.findViewById(R.id.bot_name);
        bot_address=view.findViewById(R.id.bot_address);
        bot_tell=view.findViewById(R.id.bot_tell);
        Bundle mArgs = getArguments();
        String mValue = mArgs.getString("찾아갈위치");
        geocoder = new Geocoder(getContext(), Locale.KOREAN);


        Log.e(TAG, "찾아갈주소????: "+mValue );
        String[] mobNum = mValue.split("\\|");
        bot_name.setText(mobNum[0]);
        bot_address.setText(mobNum[1]);
        bot_tell.setText(mobNum[2]);


        btn_hide_bt_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dismiss();
                Intent intent =new Intent(getContext(),directions_Activity.class);
                intent.putExtra("도착지x",mobNum[4]);
                intent.putExtra("도착지y",mobNum[3]);
                intent.putExtra("도착지주소",mobNum[1]);
                startActivity(intent);

            }
        });

        return view;
    }

    // 부모 액티비티와 연결하기위한 인터페이스
    public interface BottomSheetListener {
        void onButtonClicked(String text);


    }

}
