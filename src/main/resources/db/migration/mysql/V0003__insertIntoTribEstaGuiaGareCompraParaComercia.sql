
				
-- ============================================================================================================================================================
-- -------------------------------------------------------------------   DE SC p/ SP   ------------------------------------------------------------------------
-- ============================================================================================================================================================

-- DE SC p/ SP - Aliq == 4% (Produto IMPORTADO)
INSERT INTO trib_esta_guia  
    (icms_Aliq_interna_dest,  icms_Aliq, icms_iva, mens, is_prod_impor, tipo_guia, esta_dest_id, esta_orig_id, ncm_id, oper_id)  
    (SELECT 0.18, 0.04, 0.7148, "", 1, "GARE_ICMS", 26, 24, ncm_id, 33 FROM ncms 
         WHERE nume IN (87085099, 87083090, 84811000, 84123110, 84148019, 87089300, 84136019, 87087090, 84099190, 84099999, 84099930, 84099917, 84099959, 84212300, 84219999 ));
	
	
-- DE SC p/ SP - Aliq == 12% (Produto NACIONAL)
		
INSERT INTO trib_esta_guia  
    (icms_Aliq_interna_dest,  icms_Aliq, icms_iva, mens, is_prod_impor, tipo_guia, esta_dest_id, esta_orig_id, ncm_id, oper_id)  
    (SELECT 0.18, 0.12, 0.7148, "", 0, "GARE_ICMS", 26, 24, ncm_id, 33 FROM ncms 
         WHERE nume IN (87085099, 87083090, 84811000, 84123110, 84148019, 87089300, 84136019, 87087090, 84099190, 84099999, 84099930, 84099917, 84099959, 84212300, 84219999 ));
		
				
-- ============================================================================================================================================================
-- -------------------------------------------------------------------   DE MS p/ SP   ------------------------------------------------------------------------
-- ============================================================================================================================================================


-- DE MS p/ SP - Aliq == 4%   (Produto IMPORTADO)
INSERT INTO trib_esta_guia  
    (icms_Aliq_interna_dest,  icms_Aliq, icms_iva, mens, is_prod_impor, tipo_guia, esta_dest_id, esta_orig_id, ncm_id, oper_id)  
    (SELECT 0.18, 0.04, 0.7148, "", 1, "GARE_ICMS", 26, 12, ncm_id, 33 FROM ncms 
         WHERE nume IN (87085099, 87083090, 84811000, 84123110, 84148019, 87089300, 84136019, 87087090, 84099190, 84099999, 84099930, 84099917, 84099959, 84212300, 84219999 ));
	
		
-- DE MS p/ SP - Aliq == 12% (Produto NACIONAL)
		
INSERT INTO trib_esta_guia  
    (icms_Aliq_interna_dest,  icms_Aliq, icms_iva, mens, is_prod_impor, tipo_guia, esta_dest_id, esta_orig_id, ncm_id, oper_id)  
    (SELECT 0.18, 0.12, 0.7148, "", 0, "GARE_ICMS", 26, 12, ncm_id, 33 FROM ncms 
         WHERE nume IN (87085099, 87083090, 84811000, 84123110, 84148019, 87089300, 84136019, 87087090, 84099190, 84099999, 84099930, 84099917, 84099959, 84212300, 84219999 ));
	

-- ============================================================================================================================================================
-- -------------------------------------------------------------------   DE ES p/ SP   ------------------------------------------------------------------------
-- ============================================================================================================================================================


-- DE ES p/ SP - Aliq == 4%   (Produto IMPORTADO)
INSERT INTO trib_esta_guia  
    (icms_Aliq_interna_dest,  icms_Aliq, icms_iva, mens, is_prod_impor, tipo_guia, esta_dest_id, esta_orig_id, ncm_id, oper_id)  
    (SELECT 0.18, 0.04, 0.7148, "", 1, "GARE_ICMS", 26, 8, ncm_id, 33 FROM ncms 
         WHERE nume IN (87085099, 87083090, 84811000, 84123110, 84148019, 87089300, 84136019, 87087090, 84099190, 84099999, 84099930, 84099917, 84099959, 84212300, 84219999 ));
			
-- DE ES p/ SP - Aliq == 12% (Produto NACIONAL)
		
INSERT INTO trib_esta_guia  
    (icms_Aliq_interna_dest,  icms_Aliq, icms_iva, mens, is_prod_impor, tipo_guia, esta_dest_id, esta_orig_id, ncm_id, oper_id)  
    (SELECT 0.18, 0.12, 0.7148, "", 0, "GARE_ICMS", 26, 8, ncm_id, 33 FROM ncms 
         WHERE nume IN (87085099, 87083090, 84811000, 84123110, 84148019, 87089300, 84136019, 87087090, 84099190, 84099999, 84099930, 84099917, 84099959, 84212300, 84219999 ));
				 
		 		 
-- ============================================================================================================================================================
-- --------------------------------------------------- ABAIXO está somente os NCMS que a GABI já confirmou ---------------------------------------------------------------
-- ============================================================================================================================================================



select * from trib_esta_guia where ncm_id not in 
(select ncm_id from ncms where nume 
IN (87085099, 87083090, 84811000, 84123110, 84148019, 87089300, 84136019, 87087090, 84099190, 84099999, 84099930, 84099917, 84099959, 84212300, 84219999));

-- Deletando todas as tributações, que não tem nenhum dos ncms acima
	-- delete from trib_esta_guia where ncm_id not in 
	-- (select ncm_id from ncms where nume 
	-- IN (87085099, 87083090, 84811000, 84123110, 84148019, 87089300, 84136019, 87087090, 84099190, 84099999, 84099930, 84099917, 84099959, 84212300, 84219999));











