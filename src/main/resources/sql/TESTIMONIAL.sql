
/* Testimonial */
select  
(select email from citiuser.UserProfiles where userid = F.providerid) orgin_email,
(select email from citiuser.UserProfiles where userid = F.targetuserid) rcvr_email,
concat("Citi Ideas: Testimonial ",cast(providerid  as char),cast(targetuserid as char),cast(creationtime as char)) subj,
testimonial body,
"Testimonial" title,
from_unixtime(F.creationtime) timestamp
from Testimonials F
where creationtime >= unix_timestamp('@from_time')
and creationtime < unix_timestamp('@to_time');



