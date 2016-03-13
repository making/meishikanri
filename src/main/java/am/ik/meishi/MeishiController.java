package am.ik.meishi;

import am.ik.meishi.domain.Company;
import am.ik.meishi.domain.CompanyDao;
import am.ik.meishi.domain.Meishi;
import am.ik.meishi.domain.MeishiDao;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("meishi")
public class MeishiController {
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    ResourcePatternResolver resourcePatternResolver;
    @Autowired
    MeishiUser meishiUser;
    @Autowired
    MeishiDao meishiDao;
    @Autowired
    CompanyDao companyDao;

    @RequestMapping(method = RequestMethod.GET)
    String index(Model model,
                 @PageableDefault(sort = {"last_name", "first_name"}) Pageable pageable) {
        Map<String, String> sortMap = StreamSupport.stream(pageable.getSort().spliterator(), false)
                .collect(Collectors.toMap(Sort.Order::getProperty,
                        o -> o.getDirection().name().toLowerCase()));
        Page<Meishi> page = meishiDao.findByLoginUser(meishiUser.getEmail(), pageable);
        model.addAttribute("page", page);
        model.addAttribute("sortMap", sortMap);
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, params = "companyId")
    String byCompany(Model model,
                     @RequestParam Integer companyId,
                     @PageableDefault(sort = {"last_name", "first_name"}) Pageable pageable) {
        Map<String, String> sortMap = StreamSupport.stream(pageable.getSort().spliterator(), false)
                .collect(Collectors.toMap(Sort.Order::getProperty,
                        o -> o.getDirection().name().toLowerCase()));
        Page<Meishi> page = meishiDao.findByCompanyIdAndLoginUser(companyId, meishiUser.getEmail(), pageable);
        model.addAttribute("page", page);
        model.addAttribute("sortMap", sortMap);
        model.addAttribute("companyId", companyId);
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST)
    String post(MeishiForm form) throws IOException {
        Integer companyId = companyDao.findByCompanyName(form.getCompanyName())
                .map(Company::getCompanyId)
                .orElseGet(() -> {
                    Company company = Company.builder()
                            .companyName(form.getCompanyName())
                            .build();
                    companyDao.create(company);
                    return company.getCompanyId();
                });

        Meishi.MeishiBuilder builder = Meishi.builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .firstNameKanji(form.getFirstNameKanji())
                .lastNameKanji(form.getLastNameKanji())
                .email(form.getEmail())
                .companyId(companyId)
                .loginUser(meishiUser.getEmail());
        if (StringUtils.hasLength(form.getMeishiFront().getOriginalFilename())) {
            String extension = FilenameUtils.getExtension(form.getMeishiFront().getOriginalFilename());
            builder.meishiFront(UUID.randomUUID().toString() + "." + extension);
        }
        if (StringUtils.hasLength(form.getMeishiBack().getOriginalFilename())) {
            String extension = FilenameUtils.getExtension(form.getMeishiBack().getOriginalFilename());
            builder.meishiBack(UUID.randomUUID().toString() + "." + extension);
        }
        Meishi meishi = builder.build();
        meishiDao.create(meishi);
        if (StringUtils.hasLength(meishi.getMeishiFront())) {
            WritableResource resource = (WritableResource) resourceLoader
                    .getResource(meishiUser.bucket() + "/" + meishi.getMeishiId() + "/" + meishi.getMeishiFront());
            try (OutputStream out = resource.getOutputStream()) {
                StreamUtils.copy(form.getMeishiFront().getInputStream(), out);
            }
        }
        if (StringUtils.hasLength(meishi.getMeishiBack())) {
            WritableResource resource = (WritableResource) resourceLoader
                    .getResource(meishiUser.bucket() + "/" + meishi.getMeishiId() + "/" + meishi.getMeishiBack());
            try (OutputStream out = resource.getOutputStream()) {
                StreamUtils.copy(form.getMeishiBack().getInputStream(), out);
            }
        }
        return "redirect:/meishi";
    }

    @RequestMapping(path = "{meishiId}/{file:.+}", method = RequestMethod.GET)
    ResponseEntity<Resource> get(@PathVariable Integer meishiId, @PathVariable String file) throws IOException {
        Resource resource = resourceLoader.getResource(meishiUser.bucket() + "/" + meishiId + "/" + file);
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                .lastModified(resource.lastModified())
                .body(resource);
    }

    @RequestMapping(path = "{meishiId}")
    String delete(@PathVariable Integer meishiId) {
        Meishi meishi = meishiDao.findByMeishiIdAndLoginUser(meishiId, meishiUser.getEmail());
        meishiDao.delete(meishi);
        return "redirect:/meishi";
    }
}
