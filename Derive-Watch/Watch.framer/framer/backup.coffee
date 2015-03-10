# This imports all the layers for "destReached" into destreachedLayers3
destreachedLayers3 = Framer.Importer.load "imported/destReached"
drl = destreachedLayers3
destBack = drl.destReachedAll
dest = drl["destReached"]

destBack.opacity = 0
dest.visible = false

# This imports all the layers for "finalRoute" into finalrouteLayers2
finalrouteLayers2 = Framer.Importer.load "imported/finalRoute"
frl = finalrouteLayers2
finalBack = frl.finalRouteAll
final = frl["finalRoute"]

finalBack.opacity = 0
final.visible = false

# This imports all the layers for "bench" into benchLayers1
benchLayers1 = Framer.Importer.load "imported/bench"
bl = benchLayers1
benchBack = bl.destBenchAll
bench = bl["destBench"]

benchBack.opacity = 0
bench.visible = false


# This imports all the layers for "nextnext" into nextnextLayers
nextnextLayers = Framer.Importer.load "imported/nextnext"
nnl = nextnextLayers
nnBack = nnl.nextnextAll
nnRoute = nnl["nextnext"]

nnBack.opacity = 0
nnRoute.visible = false

# This imports all the layers for "nextRoute" into nextrouteLayers2
nextrouteLayers2 = Framer.Importer.load "imported/nextRoute"
nextRL = nextrouteLayers2
nextBack = nextRL.nextRouteAll
nextRoute = nextRL["nextRoute"]

nextBack.opacity = 0
nextRoute.visible = false

# This imports all the layers for "request" into requestLayers1
requestLayers1 = Framer.Importer.load "imported/request"
rl = requestLayers1
requestBack = rl.requestAll
request = rl["Request"]

requestBack.opacity = 0
request.visible = false

# This imports all the layers for "newRequest" into newrequestLayers
newrequestLayers = Framer.Importer.load "imported/newRequest"
newRL = newrequestLayers
newBack = newRL.newRequestAll
newRequest = newRL["newRequest"]

newBack.opacity = 1
newRequest.visible = true

isGoing = false

notification = newRL.Message
notification.on Events.Click, ->
	
	# Open envelope would be cool
	newBack.opacity = 0
	newRequest.visible = false
	requestBack.opacity = 1
	request.visible = true
	
confirm = rl.isGoing
deny = rl.notGoing
confirm.on Events.Click, ->
	
	# Confirm or Deny drift request on watch
	requestBack.opacity = 0
	request.visible = false
	nextBack.opacity = 1
	nextRoute.visible = true

walk = nextRL.walk
walk.on Events.Click, ->
	
	nextBack.opacity = 0
	nextRoute.visible = false
	nnBack.opacity = 1
	nnRoute.visible = true

nextEvent = nnl.nextnext
nextEvent.on Events.Click, ->
	
	nnBack.opacity = 0
	nnRoute.visible = false
	benchBack.opacity = 1
	bench.visible = true
	
finalDest = bl.destBench
finalDest.on Events.Click, ->
	
	benchBack.opacity = 0
	bench.visible = false
	finalBack.opacity = 1
	final.visible = true
	
destination = frl.finalRouteAll
destination.on Events.Click, ->
	
	finalBack.opacity = 0
	final.visible = false
	destBack.opacity = 1
	dest.visible = true
	
	
	





