/* Review */
select  
(select email from citiuser.UserProfiles where userid = F.creator) orgin_email,
(select email from citiuser.UserProfiles where userid = (select creator from FileSystem where nodeid = F.parentid)) rcvr_email,
concat("Citi Ideas: Post-", cast(F.parentid as char)," Review ",cast(F.nodeid as char)) subj,

concat(

(select concat("Idea Description: ",
case when data is not null then cast(data as char)
else (select group_concat(cast(attr_value as char)," ") from IdeaTextAttributesMap where ideaid = F.parentid)
end) from FileSystem where nodeid = F.parentid) ,

"Review Idea: ",

(select  case when ceil(cast(fnStripTags(extractvalue(data, "//form-data/fields[1]")) as char)) not between 1 and 9 then cast(fnStripTags(extractvalue(data, "//form-data/fields[1]")) as char) 
              when ceil(cast(fnStripTags(extractvalue(data, "//form-data/fields[2]")) as char)) not between 1 and 9 then cast(fnStripTags(extractvalue(data, "//form-data/fields[2]")) as char) 
              when ceil(cast(fnStripTags(extractvalue(data, "//form-data/fields[3]")) as char)) not between 1 and 9 then cast(fnStripTags(extractvalue(data, "//form-data/fields[3]")) as char) 
              when ceil(cast(fnStripTags(extractvalue(data, "//form-data/fields[4]")) as char)) not between 1 and 9 then cast(fnStripTags(extractvalue(data, "//form-data/fields[4]")) as char) 
              when ceil(cast(fnStripTags(extractvalue(data, "//form-data/fields[5]")) as char)) not between 1 and 9 then cast(fnStripTags(extractvalue(data, "//form-data/fields[5]")) as char) 
              when ceil(cast(fnStripTags(extractvalue(data, "//form-data/fields[6]")) as char)) not between 1 and 9 then cast(fnStripTags(extractvalue(data, "//form-data/fields[6]")) as char) 
              when ceil(cast(fnStripTags(extractvalue(data, "//form-data/fields[7]")) as char)) not between 1 and 9 then cast(fnStripTags(extractvalue(data, "//form-data/fields[7]")) as char) 
              when ceil(cast(fnStripTags(extractvalue(data, "//form-data/fields[8]")) as char)) not between 1 and 9 then cast(fnStripTags(extractvalue(data, "//form-data/fields[8]")) as char) 
              when ceil(cast(fnStripTags(extractvalue(data, "//form-data/fields[9]")) as char)) not between 1 and 9 then cast(fnStripTags(extractvalue(data, "//form-data/fields[9]")) as char) 
              when ceil(cast(fnStripTags(extractvalue(data, "//form-data/fields[10]")) as char)) not between 1 and 9 then cast(fnStripTags(extractvalue(data, "//form-data/fields[10]")) as char) 
         else "No Review Comment" end
from TaskExecution where taskid in (select nodeid from FileSystem where parentid = F.parentid ) 
and reviewid = F.nodeid and status = "Completed")

)

body,
F.title,
from_unixtime(F.modified) timestamp
from FileSystem F
where modified >= unix_timestamp('@from_time')
and modified < unix_timestamp('@to_time')
and type = 9
and flags = 1;

