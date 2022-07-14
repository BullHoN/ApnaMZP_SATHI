package com.avit.apnamzpsathi.ui.earnings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.avit.apnamzpsathi.R;
import com.avit.apnamzpsathi.databinding.FragmentEarningBinding;
import com.avit.apnamzpsathi.db.LocalDB;
import com.avit.apnamzpsathi.model.DeliverySathiDayInfo;
import com.avit.apnamzpsathi.utils.PrettyStrings;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class EarningFragment extends Fragment {

    private FragmentEarningBinding binding;
    private EarningsViewModel viewModel;
    private String deliverySathi;
    private String TAG = "EarningFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEarningBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this).get(EarningsViewModel.class);

//        MaterialDatePicker dateRangePicker =  MaterialDatePicker.Builder.dateRangePicker()
//                .setTitleText("Select Dates")
//                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),))
//                .build();
//
//        dateRangePicker.show(getChildFragmentManager(),"date_range_picker");
//        dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
//            @Override
//            public void onPositiveButtonClick(Object selection) {
//                Log.i(TAG, "onPositiveButtonClick: " + dateRangePicker.getHeaderText());
//            }
//        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });

        Date todayDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        binding.detailsOnDate.setText("Details For " + simpleDateFormat.format(todayDate));

        deliverySathi = LocalDB.getDeliverySathiDetails(getContext()).getPhoneNo();

        viewModel.getDeliverySathiInfo(getContext(),deliverySathi,simpleDateFormat.format(todayDate));

        binding.calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dateOfMonth) {
                String monthString = String.valueOf((month+1)), dateOfMonthString = String.valueOf(dateOfMonth);
                if(month+1 < 10){
                    monthString = "0" + (month+1);
                }

                if(dateOfMonth < 10){
                    dateOfMonthString = "0" + dateOfMonth;
                }

                String dateString = year + "-" + monthString + "-" + dateOfMonthString;

                binding.detailsOnDate.setText("Details For " + dateString);
                viewModel.getDeliverySathiInfo(getContext(),deliverySathi,dateString);

            }
        });

        viewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<DeliverySathiDayInfo>() {
            @Override
            public void onChanged(DeliverySathiDayInfo deliverySathiDayInfo) {
                Log.i(TAG, "onChanged: " + deliverySathiDayInfo.getTotalEarnings() + " " + deliverySathiDayInfo.getNoOfOrders());

                binding.totalEarnings.setText("₹" + deliverySathiDayInfo.getTotalEarnings());
                binding.totalOrders.setText(String.valueOf(deliverySathiDayInfo.getNoOfOrders()));
                binding.ordersEarning.setText(PrettyStrings.getPriceInRupees(deliverySathiDayInfo.getEarnings()));
                binding.totalIncentive.setText(PrettyStrings.getPriceInRupees(deliverySathiDayInfo.getIncentives()));

            }
        });

        return binding.getRoot();
    }

}