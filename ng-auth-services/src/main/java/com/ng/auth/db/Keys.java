/*
 * This file is generated by jOOQ.
 */
package com.ng.auth.db;

import org.jooq.Identity;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;

import com.ng.auth.db.tables.AppUser;
import com.ng.auth.db.tables.records.AppUserRecord;

/**
 * A class modelling foreign key relationships and constraints of tables of the
 * <code></code> schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

  // -------------------------------------------------------------------------
  // IDENTITY definitions
  // -------------------------------------------------------------------------

  public static final Identity<AppUserRecord, Long> IDENTITY_APP_USER = Identities0.IDENTITY_APP_USER;

  // -------------------------------------------------------------------------
  // UNIQUE and PRIMARY KEY definitions
  // -------------------------------------------------------------------------

  public static final UniqueKey<AppUserRecord> CONSTRAINT_7 = UniqueKeys0.CONSTRAINT_7;
  public static final UniqueKey<AppUserRecord> CONSTRAINT_76 = UniqueKeys0.CONSTRAINT_76;

  // -------------------------------------------------------------------------
  // FOREIGN KEY definitions
  // -------------------------------------------------------------------------

  // -------------------------------------------------------------------------
  // [#1459] distribute members to avoid static initialisers > 64kb
  // -------------------------------------------------------------------------

  private static class Identities0 {
    public static Identity<AppUserRecord, Long> IDENTITY_APP_USER = Internal
        .createIdentity(AppUser.APP_USER, AppUser.APP_USER.ID);
  }

  private static class UniqueKeys0 {
    public static final UniqueKey<AppUserRecord> CONSTRAINT_7 = Internal.createUniqueKey(
        AppUser.APP_USER, "CONSTRAINT_7", new TableField[] { AppUser.APP_USER.ID }, true);
    public static final UniqueKey<AppUserRecord> CONSTRAINT_76 = Internal.createUniqueKey(
        AppUser.APP_USER, "CONSTRAINT_76", new TableField[] { AppUser.APP_USER.USERNAME },
        true);
  }
}
