package in.voiceme.app.voiceme;

import android.view.View;


/**
 * Created by Nayomnik on 1/3/2017.
 */

/**
 * This asks "WasLoggedIn?".
 * This interface usually defined for behaviour of a clickable
 * object that implements this interface
 * Not only clickables can implement this. If object or component  manages
 * clickable views it also may implement one. Rather mostly.
 * E.g. BaseActivity or any view adapters.
 @see in.voiceme.app.voiceme.infrastructure.BaseActivity BaseActivity
 *@author Iskandar Huseynov (Nayomnik)
 * @version 1.0
 */
public interface WasLoggedInInterface {

    /**
     *Facilitator field  to know was user logged in or is in demo mode
     @see BaseLoginClass BaseLoginClass
     */

    BaseLoginClass mBaseLoginClass = new BaseLoginClass();

    /**
     * The object can define  how
     * to behave in this method on its taste.
     * @param  viewPrm the View object who has been clicked
     * */

    boolean processLoggedState(View viewPrm);

}
