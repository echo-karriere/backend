/*
 * This file is generated by jOOQ.
 */
package no.echokarriere.db.tables;


import no.echokarriere.db.Keys;
import no.echokarriere.db.Public;
import no.echokarriere.db.tables.records.FormRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Form extends TableImpl<FormRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.form</code>
     */
    public static final Form FORM = new Form();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FormRecord> getRecordType() {
        return FormRecord.class;
    }

    /**
     * The column <code>public.form.id</code>.
     */
    public final TableField<FormRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "");

    /**
     * The column <code>public.form.title</code>.
     */
    public final TableField<FormRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.form.description</code>.
     */
    public final TableField<FormRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.form.created_at</code>.
     */
    public final TableField<FormRecord, OffsetDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false).defaultValue(DSL.field("now()", SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "");

    /**
     * The column <code>public.form.modified_at</code>.
     */
    public final TableField<FormRecord, OffsetDateTime> MODIFIED_AT = createField(DSL.name("modified_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    private Form(Name alias, Table<FormRecord> aliased) {
        this(alias, aliased, null);
    }

    private Form(Name alias, Table<FormRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.form</code> table reference
     */
    public Form(String alias) {
        this(DSL.name(alias), FORM);
    }

    /**
     * Create an aliased <code>public.form</code> table reference
     */
    public Form(Name alias) {
        this(alias, FORM);
    }

    /**
     * Create a <code>public.form</code> table reference
     */
    public Form() {
        this(DSL.name("form"), null);
    }

    public <O extends Record> Form(Table<O> child, ForeignKey<O, FormRecord> key) {
        super(child, key, FORM);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<FormRecord> getPrimaryKey() {
        return Keys.FORM_PKEY;
    }

    @Override
    public List<UniqueKey<FormRecord>> getKeys() {
        return Arrays.<UniqueKey<FormRecord>>asList(Keys.FORM_PKEY, Keys.FORM_TITLE_KEY);
    }

    @Override
    public Form as(String alias) {
        return new Form(DSL.name(alias), this);
    }

    @Override
    public Form as(Name alias) {
        return new Form(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Form rename(String name) {
        return new Form(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Form rename(Name name) {
        return new Form(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, String, String, OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
