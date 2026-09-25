FOLDER=/home/Jahadi/backup/`date "+%Y-%m-%d"`;
mkdir $FOLDER;

mongoexport --db jahadi-i2 --collection user --jsonArray -o $FOLDER/user.json;
mongoexport --db jahadi-i2 --collection trip --jsonArray -o $FOLDER/trip.json;
mongoexport --db jahadi-i2 --collection patient --jsonArray -o $FOLDER/patient.json;
mongoexport --db jahadi-i2 --collection project --jsonArray -o $FOLDER/project.json;
mongoexport --db jahadi-i2 --collection drug --jsonArray -o $FOLDER/drug.json;
mongoexport --db jahadi-i2 --collection drug_bookmark --jsonArray -o $FOLDER/drug_bookmark.json;
mongoexport --db jahadi-i2 --collection drug_logs --jsonArray -o $FOLDER/drug_logs.json;
mongoexport --db jahadi-i2 --collection drugs_in_area --jsonArray -o $FOLDER/drugs_in_area.json;
mongoexport --db jahadi-i2 --collection external_referral_access_for_group --jsonArray -o $FOLDER/external_referral_access_for_group.json;
mongoexport --db jahadi-i2 --collection group --jsonArray -o $FOLDER/group.json;
mongoexport --db jahadi-i2 --collection module --jsonArray -o $FOLDER/module.json;
mongoexport --db jahadi-i2 --collection note --jsonArray -o $FOLDER/note.json;
mongoexport --db jahadi-i2 --collection patient_drug --jsonArray -o $FOLDER/patient_drug.json;
mongoexport --db jahadi-i2 --collection patients_in_area --jsonArray -o $FOLDER/patients_in_area.json;
mongoexport --db jahadi-i2 --collection presence_list --jsonArray -o $FOLDER/presence_list.json;
mongoexport --db jahadi-i2 --collection ware_house_access_for_group --jsonArray -o $FOLDER/ware_house_access_for_group.json;
