package com.similan.domain.repository.global;

import org.opengis.geometry.coordinate.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.OrganizationType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.lead.AffiliateLead;

@Repository
public class GlobalRepository {

  public enum SearchTargetType {

    ANYTHING(Object.class, "Any"),

    SOCIAL_PERSON(SocialPerson.class, "Person"),

    SOCIAL_ORGANIZATION_ANY(SocialOrganization.class, "Any Business"),

    SOCIAL_ORGANIZATION_RESELLER(SocialOrganization.class, "Reseller", tf(
        "SocialOrganization__organizationType", OrganizationType.RESELLER)),

    SOCIAL_ORGANIZATION_SUPPLIER(SocialOrganization.class, "Supplier", tf(
        "SocialOrganization__organizationType", OrganizationType.SUPPLIER)),

    SOCIAL_ORGANIZATION_DISTRIBUTOR(
        SocialOrganization.class,
        "Distributor",
        tf("SocialOrganization__organizationType", OrganizationType.DISTRIBUTOR)),

    SOCIAL_ORGANIZATION_LEADAFFILIATE(SocialOrganization.class,
        "Lead Affiliate", tf("SocialOrganization__organizationType",
            OrganizationType.LEADAFFILIATE)),

    AFFILIATE_LEAD(AffiliateLead.class, "AffiliateLead"),

    SOCIAL_ORGANIZATION_AGENT(SocialOrganization.class, "Agent", tf(
        "SocialOrganization__organizationType", OrganizationType.AGENT)),

    SOCIAL_ORGANIZATION_TECHNOLOGY_PARTNER(SocialOrganization.class,
        "Technology Partner", tf("SocialOrganization__organizationType",
            OrganizationType.TECHNOLOGY_PARTNER)),

    SOCIAL_ORGANIZATION_MARKETING_PARTNER(SocialOrganization.class,
        "Marketing Partner", tf("SocialOrganization__organizationType",
            OrganizationType.MARKETING_PARTNER)),

    ;

    public static class TypeFilter<T extends Enum<T>> {
      private final String fieldName;
      private final T[] values;

      @SafeVarargs
      public TypeFilter(String fieldName, T... values) {
        super();
        this.fieldName = fieldName;
        this.values = values;
      }

      public String getFieldName() {
        return fieldName;
      }

      public T[] getValues() {
        return values;
      }

    }

    private final Class<?> baseType;
    private final TypeFilter<?>[] typeFilters;
    private final String typeName;
    private final boolean searchable;

    private SearchTargetType(Class<?> baseType, String typeName,
        boolean searchable, TypeFilter<?>... typeFilters) {
      this.baseType = baseType;
      this.typeFilters = typeFilters;
      this.typeName = typeName;
      this.searchable = searchable;
    }

    private SearchTargetType(Class<?> baseType, String typeName,
        TypeFilter<?>... typeFilters) {
      this(baseType, typeName, true, typeFilters);
    }

    @SafeVarargs
    private static <T extends Enum<T>> TypeFilter<T> tf(String fieldName,
        T... values) {
      return new TypeFilter<T>(fieldName, values);
    }

    public Class<?> getBaseType() {
      return baseType;
    }

    public TypeFilter<?>[] getTypeFilters() {
      return typeFilters;
    }

    public String getTypeName() {
      return typeName;
    }

    public boolean isSearchable() {
      return searchable;
    }

  }

  public static class LocationFilter {
    public final Position position;
    public final Double distance;
    public final String keywords;

    public LocationFilter(Position position, Double distance, String keywords) {
      this.position = position;
      this.distance = distance;
      this.keywords = keywords;
    }

    public Position getPosition() {
      return position;
    }

    public Double getDistance() {
      return distance;
    }

    public String getKeywords() {
      return keywords;
    }

  }

  public <T> Page<T> search(String keywords, boolean allKeywords,
      LocationFilter locationFilter, Iterable<SearchTargetType> targetTypes,
      Pageable pageable) {
    throw new UnsupportedOperationException();
  }
}