package com.retigalo.dglozano.meetapp.util;

import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by dglozano on 23/02/18.
 */

public class AddressFormater {

    private Geocoder geocoder;

    public AddressFormater(Geocoder geocoder){
        this.geocoder = geocoder;
    }

    public String format(LatLng latlng){
        List<Address> matches = null;
        String lugarAMostrar = "Lugar no especificado";
        try {
            matches = geocoder.getFromLocation(latlng.latitude,
                    latlng.longitude,
                    1);
            Address bestMatch = (matches == null || matches.isEmpty()) ? null : matches.get(0);
            if (bestMatch != null) {
                String direccion = bestMatch.getAddressLine(0);
                lugarAMostrar = String.format("%s, %s, %s",
                        direccion.substring(0, direccion.indexOf(',')),
                        bestMatch.getLocality(),
                        bestMatch.getCountryCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lugarAMostrar;
    }

}
