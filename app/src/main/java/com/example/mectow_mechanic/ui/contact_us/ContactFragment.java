package com.example.mectow_mechanic.ui.contact_us;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.mectow_mechanic.R;
import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import com.example.mectow_mechanic.R;


public class ContactFragment extends Fragment {

    private ContactViewModel contactViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactViewModel =
                ViewModelProviders.of(this).get(ContactViewModel.class);
        ViewGroup viewGroup =(ViewGroup) inflater.inflate(R.layout.fragment_nav_contact_us, container, false);
        final TextView textView = viewGroup.findViewById(R.id.text_contact_us);
        contactViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");
        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
                .setDescription("MECTOW owes a big thanks to our customers for their continual support.\n"+"We have always tried to our best to listen for you.Feel free to get in touch with a MECTOW Customer Care Agent wheather you\n"+
                "You are currently in trouble\n"+
                "You are on the road and you need some Mechanic and Tow service\n"+
                "You are stuck in a sticky situation,one call will fixed it ")
                .addItem(adsElement)
                .create();
        viewGroup.addView(aboutPage);
        return viewGroup;
    }
    private Element createCopyright()
    {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d by Your Name", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        // copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}