package tw.com.vip_tjob;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by fusun on 2016/6/19.
 */
public class BaseAppCompatActivity extends AppCompatActivity {

    public void ShowDialModal(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(BaseAppCompatActivity.this, R.style.AlertTheme);
        dialog.setTitle("訊息提醒");
        dialog.setMessage(message);
        dialog.setPositiveButton("確認",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }
        );
        dialog.create().show();
    }

    public interface LoginInputListener
    {
        void onLoginInputComplete(String username, String password);
    }

    public static class LoginDialogFragment extends DialogFragment implements
            DialogInterface.OnClickListener
    {



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //
            String message = getArguments().getString("message");

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.login_comm, null);

            return new AlertDialog.Builder(getActivity())
                    .setView(view)
                    .setTitle("Login")
                    //.setMessage(message)
                    .setPositiveButton("OK", this)
                    .setNegativeButton("Cancel", this).create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    //Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                    LoginInputListener listener = (LoginInputListener) getActivity();
                    listener.onLoginInputComplete("Jerry", "11111");

                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    }
}
