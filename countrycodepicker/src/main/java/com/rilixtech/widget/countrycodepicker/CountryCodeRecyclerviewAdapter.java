package com.rilixtech.widget.countrycodepicker;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CountryCodeRecyclerviewAdapter extends RecyclerView.Adapter<CountryCodeRecyclerviewAdapter.ViewHolder> {

    private final CountryCodePicker mCountryCodePicker;
    private String mDefaultLocaleLanguage;

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    List<Country> countries;

    // data is passed into the constructor
    CountryCodeRecyclerviewAdapter(Context ctx, List<Country> countries, CountryCodePicker picker) {
        this.mInflater = LayoutInflater.from(ctx);
        mCountryCodePicker = picker;
        mDefaultLocaleLanguage = Locale.getDefault().getLanguage();
        this.countries = countries;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.country_code_picker_item_country, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Country country = countries.get(position);
        setData(country,holder);


    }


    private Locale getLocale(String iso) throws NullPointerException {
        return new Locale(mDefaultLocaleLanguage, iso);
    }

    private void setData(Country country, ViewHolder viewHolder) {
        if (country == null) {
            viewHolder.viewDivider.setVisibility(View.VISIBLE);
            viewHolder.tvName.setVisibility(View.GONE);
            viewHolder.tvCode.setVisibility(View.GONE);
            viewHolder.llyFlagHolder.setVisibility(View.GONE);
            return;
        }

        viewHolder.viewDivider.setVisibility(View.GONE);
        viewHolder.tvName.setVisibility(View.VISIBLE);
        viewHolder.tvCode.setVisibility(View.VISIBLE);
        viewHolder.llyFlagHolder.setVisibility(View.VISIBLE);
        Context ctx = viewHolder.tvName.getContext();
        String name = country.getName();
        String iso = country.getIso().toUpperCase();
        String countryName;
        String countryNameAndCode;
        try {
            countryName = getLocale(iso).getDisplayCountry();
        } catch (NullPointerException exception) {
            countryName = name;
        }
        if (mCountryCodePicker.isHideNameCode()) {
            countryNameAndCode = countryName;
        } else {
            countryNameAndCode = ctx.getString(R.string.country_name_and_code, countryName, iso);
        }
        viewHolder.tvName.setText(countryNameAndCode);

        if (mCountryCodePicker.isHidePhoneCode()) {
            viewHolder.tvCode.setVisibility(View.GONE);
        } else {
            viewHolder.tvCode.setText(ctx.getString(R.string.phone_code, country.getPhoneCode()));
        }

        Typeface typeface = mCountryCodePicker.getTypeFace();
        if (typeface != null) {
            viewHolder.tvCode.setTypeface(typeface);
            viewHolder.tvName.setTypeface(typeface);
        }
        viewHolder.imvFlag.setImageResource(CountryUtils.getFlagDrawableResId(country));
        int color = mCountryCodePicker.getDialogTextColor();
        if (color != mCountryCodePicker.getDefaultContentColor()) {
            viewHolder.tvCode.setTextColor(color);
            viewHolder.tvName.setTextColor(color);
        }
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return countries.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rlyMain;
        TextView tvName, tvCode;
        ImageView imvFlag;
        LinearLayout llyFlagHolder;
        View viewDivider;


        ViewHolder(View itemView) {
            super(itemView);
            rlyMain = itemView.findViewById(R.id.item_country_rly);
            tvName = itemView.findViewById(R.id.country_name_tv);
            tvCode = itemView.findViewById(R.id.code_tv);
            imvFlag = itemView.findViewById(R.id.flag_imv);
            llyFlagHolder = itemView.findViewById(R.id.flag_holder_lly);
            viewDivider = itemView.findViewById(R.id.preference_divider_view);
            itemView.setOnClickListener(this);
//            itemView.setTag(viewDivider);
//        } else {
//            viewHolder = (ViewHolder) itemView.getTag();
//        }
//        setData(country, viewHolder);

    }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Country getItem(int id) {
        return countries.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}