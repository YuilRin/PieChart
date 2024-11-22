package com.example.mobileapp.ui.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import com.example.mobileapp.Activity.LoginActivity;
import com.example.mobileapp.Activity.LoginFragment.ChooseFragment;
import com.example.mobileapp.Activity.LoginFragment.LoginFragment;
import com.example.mobileapp.Custom.CustomAdapter;
import com.example.mobileapp.R;
import com.example.mobileapp.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    ListView listView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView tvName =binding.tvName;
        String name = getUserName();

        if (name != null) {
            tvName.setText(name);
        }

        Button btnBack = binding.btnBack;
        Button btnLogout= binding.btnLogout;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("Choose", true); // Truyền flag
                startActivity(intent);
                getActivity().finish(); // Kết thúc Activity hiện tại nếu cần
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                clearUserEmail(); // Xóa email đã lưu
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("Choose", false); // Truyền flag
                startActivity(intent);
                getActivity().finish();
            }
        });
        listView = binding.ListCN;

        // Dữ liệu cho ListView
        ArrayList<String> listItems = new ArrayList<>();
        listItems.add("Giới thiêu bạn bè");
        listItems.add("Đổi tên");
        listItems.add("Đánh giá");
        listItems.add("Thông tin nhóm");
        listItems.add("Cài đặt");

        // Tạo Adapter và gán cho ListView
        CustomAdapter adapter = new CustomAdapter(getContext(), listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {  // "Đổi tên" là mục thứ hai (index 1)
                    showChangeNameDialog();
                    updateUserName();
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void clearUserEmail() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("USER_EMAIL");
        editor.apply();
    }

    private String getUserName() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("USER_NAME", null);
    }
    private void showChangeNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_name, null);
        builder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.edit_name);
        Button saveNameButton = dialogView.findViewById(R.id.btn_save_name);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Xử lý khi bấm nút Save
        saveNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editName.getText().toString().trim();
                if (newName.isEmpty()) {
                    Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    saveUserName(newName); // Lưu tên người dùng
                    updateUserName();
                    Toast.makeText(getContext(), "Name updated to " + newName, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();


                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUserName();
    }

    // Cập nhật TextView với tên đã lưu
    private void updateUserName() {
        if (getView() != null) {
            TextView userNameTextView = getView().findViewById(R.id.tvName);
            String userName = getUserName();
            if (userName != null) {
                userNameTextView.setText(userName);
            }
        }
    }
    private void saveUserName(String userName) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_NAME", userName);
        editor.apply();
    }
}