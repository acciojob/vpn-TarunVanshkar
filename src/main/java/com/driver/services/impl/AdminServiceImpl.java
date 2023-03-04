package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService
{
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password)
    {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);

        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName)
    {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(providerName);

        Admin admin = adminRepository1.findById(adminId).get();
        List<ServiceProvider> serviceProviderList;
        if(admin.getServiceProviders()==null)
        {
            serviceProviderList=new ArrayList<>();
        }
        else
        {
            serviceProviderList=admin.getServiceProviders();
        }
        serviceProviderList.add(serviceProvider);
        admin.setServiceProviders(serviceProviderList);
        serviceProvider.setAdmin(admin);
        adminRepository1.save(admin);
        //serviceProviderRepository1.save(serviceProvider);
        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception
    {
//        if(!countryName.equals("ind") || !countryName.equals("aus") || !countryName.equals("usa") || !countryName.equals("chi") || !countryName.equals("jpn"))
//        {
//            throw new Exception("Country not found");
//        }

        if(countryName==null)
        {
            throw new Exception("Country not found");
        }

        ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();

        Country country = new Country();
        CountryName countryName1 = CountryName.valueOf(countryName);
        country.setCountryName(countryName1);
        country.setCode(countryName1.toCode());
        country.setServiceProvider(serviceProvider);

        List<Country> countryList;
        if(serviceProvider.getCountryList()==null)
        {
            countryList = new ArrayList<>();
        }
        else
        {
            countryList = serviceProvider.getCountryList();
        }
        countryList.add(country);

        countryRepository1.save(country);
        serviceProviderRepository1.save(serviceProvider);
        return serviceProvider;
    }
}
