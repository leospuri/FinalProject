package in.voiceme.app.voiceme.PostsDetails;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import in.voiceme.app.voiceme.R;

/**
 * Created by harishpc on 8/16/2017.
 */
public class DrawTextToBitmap {

    // Todo code to calculate text length - https://stackoverflow.com/questions/4794484/calculate-text-size-according-to-width-of-text-area
    public static Bitmap drawTextToBitmap(Context mContext, String text) {
        try {
            /*String text_line1 = myList.getUserNicName() + " " + "said:";*/
            String text_line2 = text + " " + " ";
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.image);
            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
            if (bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            if (text_line2.length() <= 30) {
                paint.setTextSize((int) (45 * scale));
                // Todo write way to prevent cutting of words

                String desiredString = text_line2.substring(0, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 3;
                /*canvas.drawText(text_line1, x + 50 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 20 * scale, y + 60 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 60) {
                paint.setTextSize((int) (45 * scale));
                String desiredString = text_line2.substring(0, 25);
                String desiredString1 = text_line2.substring(25, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 3;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 20 * scale, y + 60 * scale, paint);
                canvas.drawText(desiredString1, x + 20 * scale, y + 120 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 90) {
                paint.setTextSize((int) (45 * scale));
                String desiredString = text_line2.substring(0, 25);
                String desiredString1 = text_line2.substring(25, 50);
                String desiredString2 = text_line2.substring(50, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 3;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 20 * scale, y + 60 * scale, paint);
                canvas.drawText(desiredString1, x + 20 * scale, y + 120 * scale, paint);
                canvas.drawText(desiredString2, x + 20 * scale, y + 180 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 120) {
                paint.setTextSize((int) (45 * scale));
                String desiredString = text_line2.substring(0, 25);
                String desiredString1 = text_line2.substring(25, 50);
                String desiredString2 = text_line2.substring(50, 75);
                String desiredString3 = text_line2.substring(75, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 3;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 20 * scale, y + 60 * scale, paint);
                canvas.drawText(desiredString1, x + 20 * scale, y + 120 * scale, paint);
                canvas.drawText(desiredString2, x + 20 * scale, y + 180 * scale, paint);
                canvas.drawText(desiredString3, x + 20 * scale, y + 240 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 150) {
                paint.setTextSize((int) (40 * scale));
                String desiredString = text_line2.substring(0, 25);
                String desiredString1 = text_line2.substring(25, 50);
                String desiredString2 = text_line2.substring(50, 75);
                String desiredString3 = text_line2.substring(75, 100);
                String desiredString4 = text_line2.substring(100, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 12;
                int y = (bitmap.getHeight() + bounds.height()) / 4;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 0 * scale, y + 60 * scale, paint);
                canvas.drawText(desiredString1, x + 0 * scale, y + 120 * scale, paint);
                canvas.drawText(desiredString2, x + 0 * scale, y + 180 * scale, paint);
                canvas.drawText(desiredString3, x + 0 * scale, y + 240 * scale, paint);
                canvas.drawText(desiredString4, x + 0 * scale, y + 300 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 180) {
                paint.setTextSize((int) (40 * scale));
                String desiredString = text_line2.substring(0, 25);
                String desiredString1 = text_line2.substring(25, 50);
                String desiredString2 = text_line2.substring(50, 75);
                String desiredString3 = text_line2.substring(75, 100);
                String desiredString4 = text_line2.substring(100, 125);
                String desiredString5 = text_line2.substring(125, 150);
                String desiredString6 = text_line2.substring(150, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 4;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 20 * scale, y + 60 * scale, paint);
                canvas.drawText(desiredString1, x + 20 * scale, y + 120 * scale, paint);
                canvas.drawText(desiredString2, x + 20 * scale, y + 180 * scale, paint);
                canvas.drawText(desiredString3, x + 20 * scale, y + 240 * scale, paint);
                canvas.drawText(desiredString4, x + 20 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString5, x + 20 * scale, y + 360 * scale, paint);
                canvas.drawText(desiredString6, x + 20 * scale, y + 420 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 210) {
                paint.setTextSize((int) (40 * scale));
                String desiredString = text_line2.substring(0, 30);
                String desiredString1 = text_line2.substring(30, 60);
                String desiredString2 = text_line2.substring(60, 90);
                String desiredString3 = text_line2.substring(90, 120);
                String desiredString4 = text_line2.substring(120, 150);
                String desiredString5 = text_line2.substring(150, 180);
                String desiredString6 = text_line2.substring(180, text_line2.length());
                /*String desiredString7 = text_line2.substring(175, text_line2.length());*/
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                /*paint.getTextBounds(desiredString7, 0, desiredString7.length(), bounds);*/
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 5;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 25 * scale, y + 50 * scale, paint);
                canvas.drawText(desiredString1, x + 25 * scale, y + 100 * scale, paint);
                canvas.drawText(desiredString2, x + 25 * scale, y + 150 * scale, paint);
                canvas.drawText(desiredString3, x + 25 * scale, y + 200 * scale, paint);
                canvas.drawText(desiredString4, x + 25 * scale, y + 250 * scale, paint);
                canvas.drawText(desiredString5, x + 25 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString6, x + 25 * scale, y + 350 * scale, paint);
                /*canvas.drawText(desiredString7, x + 0 * scale, y + 480 * scale, paint);*/
                /*Toast.makeText(PostsDetailsActivity.this,"210++"+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 240) {
                paint.setTextSize((int) (40 * scale));
                String desiredString = text_line2.substring(0, 30);
                String desiredString1 = text_line2.substring(30, 60);
                String desiredString2 = text_line2.substring(60, 90);
                String desiredString3 = text_line2.substring(90, 120);
                String desiredString4 = text_line2.substring(120, 150);
                String desiredString5 = text_line2.substring(150, 180);
                String desiredString6 = text_line2.substring(180, 210);
                String desiredString7 = text_line2.substring(210, text_line2.length());
                /*String desiredString8 = text_line2.substring(200, text_line2.length());*/
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                paint.getTextBounds(desiredString7, 0, desiredString7.length(), bounds);
                /*paint.getTextBounds(desiredString8, 0, desiredString8.length(), bounds);*/
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 5;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 0 * scale, y + 50 * scale, paint);
                canvas.drawText(desiredString1, x + 0 * scale, y + 100 * scale, paint);
                canvas.drawText(desiredString2, x + 0 * scale, y + 150 * scale, paint);
                canvas.drawText(desiredString3, x + 0 * scale, y + 200 * scale, paint);
                canvas.drawText(desiredString4, x + 0 * scale, y + 250 * scale, paint);
                canvas.drawText(desiredString5, x + 0 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString6, x + 0 * scale, y + 350 * scale, paint);
                canvas.drawText(desiredString7, x + 0 * scale, y + 400 * scale, paint);
                /*canvas.drawText(desiredString8, x + 0 * scale, y + 540 * scale, paint);*/
                /*Toast.makeText(PostsDetailsActivity.this,"240++"+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 270) {
                paint.setTextSize((int) (35 * scale));
                String desiredString = text_line2.substring(0, 30);
                String desiredString1 = text_line2.substring(30, 60);
                String desiredString2 = text_line2.substring(60, 90);
                String desiredString3 = text_line2.substring(90, 120);
                String desiredString4 = text_line2.substring(120, 150);
                String desiredString5 = text_line2.substring(150, 180);
                String desiredString6 = text_line2.substring(180, 210);
                String desiredString7 = text_line2.substring(210, 240);
                String desiredString8 = text_line2.substring(240, text_line2.length());
                /*String desiredString9 = text_line2.substring(225, 250);
                String desiredString10 = text_line2.substring(250, text_line2.length());*/
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                paint.getTextBounds(desiredString7, 0, desiredString7.length(), bounds);
                paint.getTextBounds(desiredString8, 0, desiredString8.length(), bounds);
               /* paint.getTextBounds(desiredString9, 0, desiredString9.length(), bounds);
                paint.getTextBounds(desiredString10, 0, desiredString10.length(), bounds);*/
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 10;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 25 * scale, y + 50 * scale, paint);
                canvas.drawText(desiredString1, x + 25 * scale, y + 100 * scale, paint);
                canvas.drawText(desiredString2, x + 25 * scale, y + 150 * scale, paint);
                canvas.drawText(desiredString3, x + 25 * scale, y + 200 * scale, paint);
                canvas.drawText(desiredString4, x + 25 * scale, y + 250 * scale, paint);
                canvas.drawText(desiredString5, x + 25 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString6, x + 25 * scale, y + 350 * scale, paint);
                canvas.drawText(desiredString7, x + 25 * scale, y + 400 * scale, paint);
                canvas.drawText(desiredString8, x + 25 * scale, y + 450 * scale, paint);
                /*canvas.drawText(desiredString9, x + 25 * scale, y + 600 * scale, paint);
                canvas.drawText(desiredString10, x + 25 * scale, y + 660 * scale, paint);*/
                /*Toast.makeText(PostsDetailsActivity.this,"270++"+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 300) {
                paint.setTextSize((int) (35 * scale));
                String desiredString = text_line2.substring(0, 30);
                String desiredString1 = text_line2.substring(30, 60);
                String desiredString2 = text_line2.substring(60, 90);
                String desiredString3 = text_line2.substring(90, 120);
                String desiredString4 = text_line2.substring(120, 150);
                String desiredString5 = text_line2.substring(150, 180);
                String desiredString6 = text_line2.substring(180, 210);
                String desiredString7 = text_line2.substring(210, 240);
                String desiredString8 = text_line2.substring(240, 270);
                String desiredString9 = text_line2.substring(270, text_line2.length());
                /*String desiredString10 = text_line2.substring(250, 275);
                String desiredString11 = text_line2.substring(275, text_line2.length());*/
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                paint.getTextBounds(desiredString7, 0, desiredString7.length(), bounds);
                paint.getTextBounds(desiredString8, 0, desiredString8.length(), bounds);
                paint.getTextBounds(desiredString9, 0, desiredString9.length(), bounds);
                /*paint.getTextBounds(desiredString10, 0, desiredString10.length(), bounds);
                paint.getTextBounds(desiredString11, 0, desiredString11.length(), bounds);*/
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 10;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 25 * scale, y + 50 * scale, paint);
                canvas.drawText(desiredString1, x + 25 * scale, y + 100 * scale, paint);
                canvas.drawText(desiredString2, x + 25 * scale, y + 150 * scale, paint);
                canvas.drawText(desiredString3, x + 25 * scale, y + 200 * scale, paint);
                canvas.drawText(desiredString4, x + 25 * scale, y + 250 * scale, paint);
                canvas.drawText(desiredString5, x + 25 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString6, x + 25 * scale, y + 350 * scale, paint);
                canvas.drawText(desiredString7, x + 25 * scale, y + 400 * scale, paint);
                canvas.drawText(desiredString8, x + 25 * scale, y + 450 * scale, paint);
                canvas.drawText(desiredString9, x + 25 * scale, y + 500 * scale, paint);
                /*canvas.drawText(desiredString10, x + 25 * scale, y + 660 * scale, paint);
                canvas.drawText(desiredString11, x + 25 * scale, y + 720 * scale, paint);*/
                /*Toast.makeText(PostsDetailsActivity.this,"300++"+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else {
                paint.setTextSize((int) (35 * scale));
                String desiredString = text_line2.substring(0, 35);
                String desiredString1 = text_line2.substring(35, 70);
                String desiredString2 = text_line2.substring(70, 105);
                String desiredString3 = text_line2.substring(105, 140);
                String desiredString4 = text_line2.substring(140, 175);
                String desiredString5 = text_line2.substring(175, 210);
                String desiredString6 = text_line2.substring(210, 245);
                String desiredString7 = text_line2.substring(245, 280);
                String desiredString8 = text_line2.substring(280, 305);
                //   String desiredString9 = text_line2.substring(301, text_line2.length());
                /*String desiredString11 = text_line2.substring(301, text_line2.length());*/
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                paint.getTextBounds(desiredString7, 0, desiredString7.length(), bounds);
                paint.getTextBounds(desiredString8, 0, desiredString8.length(), bounds);
                //    paint.getTextBounds(desiredString9, 0, desiredString9.length(), bounds);
                /*paint.getTextBounds(desiredString9, 0, desiredString9.length(), bounds);*/
                /*paint.getTextBounds(desiredString10, 0, desiredString10.length(), bounds);*/
                /*paint.getTextBounds(desiredString11, 0, desiredString11.length(), bounds);*/

                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 10;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 25 * scale, y + 50 * scale, paint);
                canvas.drawText(desiredString1, x + 25 * scale, y + 100 * scale, paint);
                canvas.drawText(desiredString2, x + 25 * scale, y + 150 * scale, paint);
                canvas.drawText(desiredString3, x + 25 * scale, y + 200 * scale, paint);
                canvas.drawText(desiredString4, x + 25 * scale, y + 250 * scale, paint);
                canvas.drawText(desiredString5, x + 25 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString6, x + 25 * scale, y + 350 * scale, paint);
                canvas.drawText(desiredString7, x + 25 * scale, y + 400 * scale, paint);
                canvas.drawText(desiredString8, x + 25 * scale, y + 450 * scale, paint);
                //  canvas.drawText(desiredString9 + "...", x + 25 * scale, y + 500 * scale, paint);
                /*canvas.drawText(desiredString9+"...", x + 25 * scale, y + 500 * scale, paint);*/
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
                /*canvas.drawText(desiredString10, x + 25 * scale, y + 660 * scale, paint);*/
                /*canvas.drawText(desiredString11+"...", x + 25 * scale, y + 720 * scale, paint);*/
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
