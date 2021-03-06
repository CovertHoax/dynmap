package org.dynmap.regions;

import java.io.File;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.util.config.Configuration;
import org.dynmap.ConfigurationNode;
import org.dynmap.Log;
import org.dynmap.web.HttpRequest;
import org.dynmap.web.HttpResponse;
import org.dynmap.web.Json;
import org.dynmap.web.handlers.FileHandler;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

public class RegionHandler extends FileHandler {
    private ConfigurationNode regions;
    private String regiontype;
    private TownyConfigHandler towny;
    private FactionsConfigHandler factions;
    public RegionHandler(ConfigurationNode regions) {
        this.regions = regions;
        regiontype = regions.getString("name", "WorldGuard");
        if(regiontype.equals("Towny")) {
            towny = new TownyConfigHandler(regions);
        }
        else if(regiontype.equals("Factions")) {
            factions = new FactionsConfigHandler(regions);
        }
    }
    @Override
    protected InputStream getFileInput(String path, HttpRequest request, HttpResponse response) {
        if(regions == null)
            return null;
        /* Right path? */
        if(path.endsWith(".json") == false)
            return null;

        String worldname = path.substring(0, path.lastIndexOf(".json"));
        Configuration regionConfig = null;
        File infile;
        String regionFile;
        Map<?, ?> regionData;
        
        if(regiontype.equals("Towny")) {
            regionData = towny.getRegionData(worldname);
        }
        else if(regiontype.equals("Factions")) {
            regionData = factions.getRegionData(worldname);
        }
        else if(regiontype.equals("Residence")) {
            File f = new File("plugins/Residence/Save/Worlds", "res_" + worldname + ".yml");
            if(f.exists()) {
                regionConfig = new org.bukkit.util.config.Configuration(f);
            }
            else {
                f = new File("plugins/Residence", "res.yml");
                if(f.exists()) {
                    regionConfig = new org.bukkit.util.config.Configuration(f);
                }
            }
            if(regionConfig == null) return null;
            regionConfig.load();
            regionData = (Map<?, ?>) regionConfig.getProperty("Residences");
            if(regionData == null) {
                Log.severe("Region data from " + f.getPath() + " does not include basenode 'Residences'");
                return null;
            }
        }
        else {
            /* If using worldpath, format is either plugins/<plugin>/<worldname>/<filename> OR 
             * plugins/<plugin>/worlds/<worldname>/<filename>
             */
            File basepath = new File("plugins", regiontype);
            if(basepath.exists() == false)
                return null;
            if(regions.getBoolean("useworldpath", false)) {
                regionFile = worldname + "/" + regions.getString("filename", "regions.yml");
                infile = new File(basepath, regionFile);
                if(!infile.exists()) {
                    infile = new File(basepath, "worlds/" + regionFile);
                }
            }
            else {  /* Else, its plugins/<plugin>/<filename> */
                regionFile = regions.getString("filename", "regions.yml");
                infile = new File(basepath, regionFile);
            }
            if(infile.exists()) {
                regionConfig = new Configuration(infile);
            }
            //File didn't exist
            if(regionConfig == null)
                return null;
            regionConfig.load();
            /* Parse region data and store in MemoryInputStream */
            String bnode = regions.getString("basenode", "regions");
            regionData = (Map<?, ?>) regionConfig.getProperty(bnode);
            if(regionData == null) {
                Log.severe("Region data from " + infile.getPath() + " does not include basenode '" + bnode + "'");
                return null;
            }
        }
        /* See if we have explicit list of regions to report - limit to this list if we do */
        RegionsComponent.filterOutHidden(regions.getStrings("visibleregions", null), regions.getStrings("hiddenregions", null), regionData, regiontype);

        try {
            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            fos.write(Json.stringifyJson(regionData).getBytes());
            fos.close();
            return new ByteArrayInputStream(fos.toByteArray());
        } catch (FileNotFoundException ex) {
            log.log(Level.SEVERE, "Exception while writing JSON-file.", ex);
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "Exception while writing JSON-file.", ioe);
        }        
        return null;
    }
}
