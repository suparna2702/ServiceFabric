package com.similan.framework.geo;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;

import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;
import com.google.code.geocoder.model.LatLngBounds;
import com.google.common.base.Joiner;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.similan.domain.entity.util.AddressDto;

@Component
public class GoogleGeocoder implements Geocoder {
  @Getter @Setter
  private String clientId;
  @Getter @Setter
  private String clientKey;

  private com.google.code.geocoder.Geocoder geocoder; 
  
  @PostConstruct
  public void initialize() {
    if (clientId == null ^ clientKey == null) {
      throw new IllegalStateException(
          "Both clientId and clientKey must be set, or none should be");
    }
    if (clientId == null) {
      geocoder = new com.google.code.geocoder.Geocoder();
    } else {
      try {
      geocoder = new com.google.code.geocoder.Geocoder(clientId, clientKey);
      } catch (InvalidKeyException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  public AddressDto[] geocode(String address, GeoBounds bounds, String region,
      String language) {
    return geocode(address, null, bounds, region, language);
  }

  public AddressDto[] geocode(double latitude, double longitude,
      GeoBounds bounds, String region, String language) {
    return geocode(
        null,
        new LatLng(BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude)),
        bounds, region, language);
  }

  private AddressDto[] geocode(String address, LatLng location,
      GeoBounds bounds, String region, String language) {
    GeocoderRequest request = new GeocoderRequest();
    if (address != null) {
      request.setAddress(address);
    }
    if (location != null) {
      request.setLocation(location);
    }
    if (bounds != null) {
      LatLng southwest = new LatLng(
          BigDecimal.valueOf(bounds.getMinLatitude()),
          BigDecimal.valueOf(bounds.getMinLongitude()));
      LatLng northeast = new LatLng(
          BigDecimal.valueOf(bounds.getMaxLatitude()),
          BigDecimal.valueOf(bounds.getMaxLongitude()));
      request.setBounds(new LatLngBounds(southwest, northeast));
    }
    if (region != null) {
      request.setRegion(region);
    }
    if (language != null) {
      request.setLanguage(language);
    }
    GeocodeResponse response = geocoder.geocode(request);

    GeocoderStatus status = response.getStatus();
    if (status == GeocoderStatus.ZERO_RESULTS) {
      return new AddressDto[0];
    } else if (status != GeocoderStatus.OK) {
      throw new IllegalStateException("Error while geocoding, "
          + status.name()
          + ":"
          + "address="
          + (address == null ? "<null>" : address)
          + "location=<null>"
          + (location == null ? "" : "[" + location.getLat() + ";"
              + location.getLng() + ")") + "region="
          + (region == null ? "<null>" : region) + "language="
          + (language == null ? "<null>" : language));
    }
    List<GeocoderResult> results = response.getResults();
    AddressDto[] geoLocations = new AddressDto[results.size()];
    int index = 0;
    for (GeocoderResult result : results) {
      LatLng resultLocation = result.getGeometry().getLocation();
      double latitude = resultLocation.getLat().doubleValue();
      double longitude = resultLocation.getLng().doubleValue();

      ListMultimap<String, String> components = mapComponents(result
          .getAddressComponents());
      String number = findComponent(components, "street_number");
      String street = findComponent(components, "route");
      String city = findComponent(components, "locality");
      String area = findComponent(components, "administrative_area_level_3");
      String county = findComponent(components, "administrative_area_level_2");
      String state = findComponent(components, "administrative_area_level_1");
      String zipCode = findComponent(components, "postal_code");
      String country = findComponent(components, "country");
      String formattedAddress = result.getFormattedAddress();
      String searchableAddress = buildSearchableAddress(result
          .getAddressComponents());

      AddressDto geoLocation = new AddressDto("", "", latitude, longitude,
          number, street, city, area, county, state, zipCode, country,
          formattedAddress, searchableAddress);
      geoLocations[index++] = geoLocation;
    }
    return geoLocations;
  }

  private String buildSearchableAddress(
      List<GeocoderAddressComponent> components) {
    Set<String> set = new LinkedHashSet<String>(components.size() * 2);
    for (GeocoderAddressComponent component : components) {
      String longName = component.getLongName();
      String shortName = component.getShortName();
      set.add(longName);
      set.add(shortName);
    }
    Joiner joiner = Joiner.on(" ");
    return joiner.join(set);
  }

  private String findComponent(ListMultimap<String, String> components,
      String... types) {
    for (String type : types) {
      List<String> longComponents = components.get(type + "_long");
      if (!longComponents.isEmpty()) {
        return longComponents.get(0);
      } else {
        List<String> shortComponents = components.get(type + "_short");
        if (!shortComponents.isEmpty()) {
          return shortComponents.get(0);
        }
      }
    }
    return null;
  }

  private ListMultimap<String, String> mapComponents(
      List<GeocoderAddressComponent> components) {
    ListMultimap<String, String> map = LinkedListMultimap.create(components
        .size() * 2);
    for (GeocoderAddressComponent component : components) {
      String longName = component.getLongName();
      String shortName = component.getShortName();
      List<String> types = component.getTypes();
      for (String type : types) {
        map.put(type + "_long", longName);
        map.put(type + "_short", shortName);
      }
    }
    return map;
  }

}
