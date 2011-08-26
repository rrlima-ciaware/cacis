channel stop *
channel undeploy *

channel remove XSLT-CCD-Transformer
channel remove WS2F-CDF-XCCD
channel remove WS2F-XCCD-CDF

import "${build.outputDirectory}/channels/XSLT-CCD-Transformer.xml" force
import "${build.outputDirectory}/channels/WS2F-CDF-XCCD.xml" force
import "${build.outputDirectory}/channels/WS2F-XCCD-CDF.xml" force

deploy
channel start *
channel list