channel stop *
channel remove *
import "${build.outputDirectory}/channels/AcceptCanonical_Channel.xml" force
deploy
channel start *
channel list