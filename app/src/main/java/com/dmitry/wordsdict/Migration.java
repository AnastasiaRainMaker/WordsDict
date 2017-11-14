package com.dmitry.wordsdict;

import com.dmitry.wordsdict.model.WordModelRealm;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by dmitry on 6/4/17.
 */

class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm dynamicRealm, long l, long l1) {
        RealmSchema schema = dynamicRealm.getSchema();

        if (l == 8) {
            RealmObjectSchema personSchema = schema.get("WordModelRealm");
            personSchema
                    .removeField("priority");
//            personSchema
//                    .removeField("wordPriority");
//            personSchema
//                    .addField("wordPriority", Integer.class, FieldAttribute.REQUIRED).setNullable("wordPriority", true);
            l++;
        }
    }
}
