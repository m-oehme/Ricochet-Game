package de.htw_berlin.ris.ricochet.Entities;



import de.htw_berlin.ris.ricochet.objects.GameObject;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;


public class ContactListener implements org.jbox2d.callbacks.ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Object colObj = contact.m_fixtureA.getBody().getUserData();
        Object colObjOther = contact.m_fixtureB.getBody().getUserData();
        boolean isGameObject = colObj instanceof GameObject;
        if (isGameObject){
            ((GameObject) colObj).StartContact(((GameObject)colObjOther));
        }
        isGameObject = colObjOther instanceof GameObject;
        if (isGameObject){
            ((GameObject) colObjOther).StartContact(((GameObject)colObj));
        }
    }

    @Override
    public void endContact(Contact contact) {
        Object colObj = contact.m_fixtureA.getBody().getUserData();
        Object colObjOther = contact.m_fixtureB.getBody().getUserData();
        boolean isGameObject = colObj instanceof GameObject;
        if (isGameObject){
            ((GameObject) colObj).EndContact(((GameObject)colObjOther));
        }
        isGameObject = colObjOther instanceof GameObject;
        if (isGameObject){
            ((GameObject) colObjOther).EndContact(((GameObject)colObj));
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}