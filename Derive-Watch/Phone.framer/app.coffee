# This imports all the layers for "features" into featuresLayers3
featuresLayers3 = Framer.Importer.load "imported/features"
fl = featuresLayers3
features = fl["Features"]
featBack = fl.featuresAll

featBack.opacity = 0
features.visible = false

# This imports all the layers for "destination" into destinationLayers2
destinationLayers2 = Framer.Importer.load "imported/destination"
dl = destinationLayers2
destination = dl["Destination"]
destBack = dl.destinationAll

destBack.opacity = 0
destination.visible = false

# This imports all the layers for "categories" into categoriesLayers1
categoriesLayers1 = Framer.Importer.load "imported/categories"
cl = categoriesLayers1
categories = cl["Categories"]
catBack = cl.categoriesAll

catBack.opacity = 0
categories.visible = false

# This imports all the layers for "people" into peopleLayers
peopleLayers = Framer.Importer.load "imported/people"
pl = peopleLayers
people = pl["People"]
peopleBack = pl.peopleAll

peopleBack.opacity = 1
people.visible = true

pl.isCheckedMe.opacity = 0
pl.isCheckedBerke.opacity = 0
pl.isCheckedNardo.opacity = 0
pl.isCheckedAida.opacity = 0
pl.Notification.opacity = 0
fl.checkCem.opacity = 0
fl.checkP.opacity = 0
fl.checkD.opacity = 0
fl.checkB.opacity = 0
fl.checkS.opacity = 0
fl.checkF.opacity = 0
fl.checkM.opacity = 0
fl.checkCh.opacity = 0

isDestinationChosen = false
isBerkeClicked = false
isNardoClicked = false
isAidaClicked = false
isCoreyClicked = false
isParkClicked = false
isCemeteryClicked = false
isDangerClicked = false
isBenchClicked = false
isShopClicked = false
isFountainClicked = false
isMusicClicked = false
isChurchClicked = false

# Wierd bug with this
sendNotification = true

# Slide tab over to Categories
slider = pl.activeTab
slider.draggable.enabled = true
slider.draggable.speedY = 0

slider.on Events.Click, ->
	
	# You chose a dest and friends to invite but decided to start over for some reason
	if isDestinationChosen == true
		pl.isCheckedBerke.opacity = 0
		pl.isCheckedNardo.opacity = 0
		pl.isCheckedAida.opacity = 0
		isDestinationChosen = false
	
	slider.x  < -120
	# Dragged enough to move to next slide after delay
	slider.animate
		properties:
			x: +375
		time: 0.1
		# Hide People layer and show Categories layer
		peopleBack.opacity = 0
		people.visible = false		
		catBack.opacity = 1
		categories.visible = true
		
# Listener for choosing destination
destChoice = cl.Coffee
destChoice.on Events.Click, ->
	
	catBack.opacity = 0
	categories.visible = false
	destBack.opacity = 1
	destination.visible = true
	
# Listener for final destination
finalDest = dl.local123
finalDest.on Events.Click, ->
	
	destBack.opacity = 0
	destination.visible = false
	featBack.opacity = 1
	features.visible = true

# Listener for features during drift
inviteClicked = fl.invite
inviteClicked.on Events.Click, ->
	
	isDestinationChosen = true
	featBack.opacity = 0
	features.visible = false
	slider.animate
		properties:
			x: 0
		time: 0.1
	peopleBack.opacity = 1
	people.visible = true
	
# Don't send notification to watch if you haven't selected friends => selecting a final destination
if isCoreyClicked == true and isBerkeClicked == true or isNardoClicked == true or isAidaClicked == true

	sendNotification = true

# Listener for sendingNotification to watch
send = pl.sendRequest
send.on Events.Click, ->
	
	if sendNotification == true
		# Send notification to watch
		pl.Notification.opacity = 1
	
# Listeners for Features selected
cemetery = fl.checkCem
cemetery.on Events.Click, ->
	
	if isCemeteryClicked == true
		cemetery.opacity = 0
		isCemeteryClicked = false
		return
	else
		cemetery.opacity = 1
		isCemeteryClicked = true

park = fl.checkP
park.on Events.Click, ->
	
	if isParkClicked == true
		park.opacity = 0
		isParkClicked = false
		return
	else
		park.opacity = 1
		isParkClicked = true

danger = fl.checkD
danger.on Events.Click, ->
	
	if isDangerClicked == true
		danger.opacity = 0
		isDangerClicked = false
		return
	else
		danger.opacity = 1
		isDangerClicked = true

bench = fl.checkB
bench.on Events.Click, ->
	
	if isBenchClicked == true
		bench.opacity = 0
		isBenchClicked = false
		return
	else
		bench.opacity = 1
		isBenchClicked = true
		
shop = fl.checkS
shop.on Events.Click, ->
	
	if isShopClicked == true
		shop.opacity = 0
		isShopClicked = false
		return
	else
		shop.opacity = 1
		isShopClicked = true
		
fountain = fl.checkF
fountain.on Events.Click, ->
	
	if isFountainClicked == true
		fountain.opacity = 0
		isFountainClicked = false
		return
	else
		fountain.opacity = 1
		isFountainClicked = true
		
music = fl.checkM
music.on Events.Click, ->
	
	if isMusicClicked == true
		music.opacity = 0
		isMusicClicked = false
		return
	else
		music.opacity = 1
		isMusicClicked = true
		
church = fl.checkCh
church.on Events.Click, ->
	
	if isChurchClicked == true
		church.opacity = 0
		isChurchClicked = false
		return
	else
		church.opacity = 1
		isChurchClicked = true
	
# Listeners to invite friends once final destinatoin chosen
inviteBerke = pl.notCheckedBerke
inviteBerke.on Events.Click, ->
	
	if isBerkeClicked == true
		pl.isCheckedBerke.opacity = 0
		isBerkeClicked = false
		return
		
	if isDestinationChosen == true	
		pl.isCheckedBerke.opacity = 1
		isBerkeClicked = true

inviteNardo = pl.notCheckedNardo
inviteNardo.on Events.Click, ->
	
	if isNardoClicked == true
		pl.isCheckedNardo.opacity = 0
		isNardoClicked = false
		return
	
	if isDestinationChosen == true	
		pl.isCheckedNardo.opacity = 1
		isNardoClicked = true
	
inviteAida = pl.notCheckedAida
inviteAida.on Events.Click, ->
	
	if isAidaClicked == true
		pl.isCheckedAida.opacity = 0
		isAidaClicked = false
		return
	
	if isDestinationChosen == true	
		pl.isCheckedAida.opacity = 1
		isAidaClicked = true

curious = pl.isCheckedMe
curious.on Events.Click, ->
	
	if isCoreyClicked == true
		pl.isCheckedMe.opacity = 0
		isCoreyClicked = false
		return
	else
		pl.isCheckedMe.opacity = 1
		isCoreyClicked = true
