package be.app.sb.timereports;

import be.app.sb.db.tables.daos.TimereportDao;
import be.app.sb.db.tables.pojos.Timereport;
import org.jooq.Configuration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

//Define this Controller as REST
@RestController
@RequestMapping("/timereports")
public class TimereportController {

    private final TimereportDao timereportDao;

    private final TimereportService timereportService;

    public TimereportController(Configuration jooqConfiguration, TimereportService timereportService) {
        this.timereportDao = new TimereportDao(jooqConfiguration);
        this.timereportService = timereportService;
    }

    /**
     * HTTP POST request that let an employee add its own timereport.
     * Does not work for adding timereports of other employees
     * Only accessible by ROLE_EM (see @Secured annotation)
     * @param user User object being built at authentication (See UserDetailsServiceImpl class)
     * @param newTimereport @RequestBody of the new timereport to add
     * @return the time report added
     */
    @Secured("ROLE_EM")
    @PostMapping("/")
    public Timereport newTimeReport(@AuthenticationPrincipal User user,
                                  @RequestBody Timereport newTimereport) {
        return this.timereportService.insertNewTimereport(user, newTimereport);
    }

    /**
     * HTTP GET request that generate a timesheet per department and per date
     * Does not work for adding timereports of other employees
     * Only accessible by ROLE_EM (see @Secured annotation)
     * @param user User object being built at authentication (See UserDetailsServiceImpl class)
     * @return the time report added
     */
    //@Secured("ROLE_")
    @GetMapping("/")
    public String generateTimesheet(@AuthenticationPrincipal User user) {
        return this.timereportService.exportTimereportAsCsv(user);
    }
}