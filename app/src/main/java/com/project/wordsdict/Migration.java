package com.project.wordsdict;

import android.support.annotation.NonNull;
import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

class Migration implements RealmMigration {
    @SuppressWarnings("UnusedAssignment")
    @Override
    public void migrate(@NonNull DynamicRealm dynamicRealm, long l, long l1) {
        RealmSchema schema = dynamicRealm.getSchema();

        if (l == 8) {
            RealmObjectSchema personSchema = schema.get("WordModelRealm");
            assert personSchema != null;
            personSchema
                    .removeField("priority");
           l++;
        }
    }
}
