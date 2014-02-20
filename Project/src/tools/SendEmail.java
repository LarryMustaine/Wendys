package tools;

import com.larrystudio.blondie.R;

import android.content.Context;
import android.content.Intent;

public class SendEmail {

	public SendEmail(Context context){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hungry.developers@gmail.com"});
		intent.putExtra(Intent.EXTRA_SUBJECT, "Video Games Music Request");
		intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_text));

		context.startActivity(Intent.createChooser(intent, "Send Email"));
	}
}