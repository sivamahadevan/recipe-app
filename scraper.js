var fs = require('fs');
var util = require('util');
var request = require('request');
var cheerio = require('cheerio');
var randomstring = require('randomstring');
var sqlite3 = require('sqlite3').verbose();
var Flickr = require("flickrapi");
var flickrOptions = {
  api_key: '0aceca87de613e2b1d3c41c031e081ed',
  secret: '77e57f69533e5d07'
};

function isKeyword(ingredientWordList, descriptionWordList) {
  var numFound = 0;
  for(var i = 0; i < ingredientWordList.length; i++) {
    var iw = ingredientWordList[i];
    for(var j = 0; j < descriptionWordList.length; j++) {
      var dw = descriptionWordList[j];
      if(dw.indexOf(iw) !== -1) {
        numFound++;
        break;
      }
    }
  }

  var keyword = numFound === ingredientWordList.length;
  return keyword;
}

function scrapeOneRecipe(recipes, index, total, db, INGREDIENTS) {
  var recipeData = recipes[index];

  request(recipeData.recipeUrl, function(error, response, html) {
    if (error) return console.error('error clicking recipe link: ', error);

    var $$ = cheerio.load(html);
    var recipeDescription = $$('.slot-6-7-8 > div').first().contents().filter(function() {
      return this.nodeType == 3;
    }).first().text();

    // Getting the main recipe information
    var recipeTableTag = $$('table');
    var recipeAvatar = 'http://www.whfoods.com/' + recipeTableTag.find('img').attr('src');
    var recipePrepTime = recipeTableTag.find("td b:contains('Prep and Cook Time')").parent().contents().filter(function() {
      return this.nodeType == 3;
    }).eq(1).text().trim();
    var recipeInsertStatement = db.prepare("INSERT INTO recipes (name, avatar, description, prep_time) VALUES (?, ?, ?, ?)");
    recipeInsertStatement.run(recipeData.recipeName, recipeAvatar, recipeDescription, recipePrepTime, function(err) {
      if (err) return console.error('error inserting recipe into database: ', err);

      var recipeId = this.lastID;
      console.log(util.format("(%d) - %s", recipeId, recipeData.recipeName));
      // creating the recipe-ingredients
      var recipeIngredientInsertStatement = db.prepare("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, description) VALUES (?, ?, ?)");

      var uniqueIngredientIds = [];
      var recipeIngredients = recipeTableTag.find('.llist li').each(function(i, ingredientEl) {
        var recipeIngredientDescription = $$(ingredientEl).text();
        var recipeIngredientWordList = recipeIngredientDescription.split(' ').map(function(word) {
          return word.toLowerCase();
        });

        for(var i = 0; i < INGREDIENTS.length; i++) {
          var INGREDIENT = INGREDIENTS[i];

          if(isKeyword(INGREDIENT.wordList, recipeIngredientWordList)) {
            if(uniqueIngredientIds.indexOf(INGREDIENT.id) === -1) {
              recipeIngredientInsertStatement.run(recipeId, INGREDIENT.id, recipeIngredientDescription);
              uniqueIngredientIds.push(INGREDIENT.id);
            }
            break;
          }
        }
      });
      recipeIngredientInsertStatement.finalize(function() {
        console.log('inserted all ingredients');
        var directionInsertStatement = db.prepare("INSERT INTO recipe_directions (recipe_id, description) VALUES (?, ?)");

        $$('ol li').each(function(i, directionEl) {
          var directionDescription = $$(directionEl).text().trim();
          directionInsertStatement.run(recipeId, directionDescription);
        });

        directionInsertStatement.finalize(function() {
          console.log('inserted all directions');
          // DONE
          index++;
          if (index === total) {
            console.log('done');
          } else {
            scrapeOneRecipe(recipes, index, total, db, INGREDIENTS);
          }
        });
      });
    });
  });
}

// SCRAPING RECIPES
function scrapeRecipes() {
  var db = new sqlite3.Database('./app.db');

  db.all('SELECT rowid, name FROM ingredients order by rowid', function(err, rows) {
    var INGREDIENTS = rows.map(function(row) {
      return {
        id: row.rowid,
        wordList: row.name.split(' ')
          .map(function(word) { return word.replace(/[^a-zA-Z]/g, '').toLowerCase(); })
          .filter(function(word) { return word !== ''; })
      };
    });

    request('http://www.whfoods.com/recipestoc.php', function(error, response, html) {
      var $ = cheerio.load(html);
      var processed = 0;
      var total = 0;
      var recipesToScrape = [];
      var doneBList = false;

      $('ul.blist').each(function(i, ulEl) {
        if (!doneBList) {
          doneBList = $(ulEl).next().is('hr');
          $(ulEl).children().each(function(i, liEl) {
            total++;
            var aTag = $(liEl).find(':first-child');
            var recipeName = aTag.text();
            recipesToScrape.push({
              recipeName: recipeName,
              recipeUrl: 'http://www.whfoods.com/' + aTag.attr('href')
            });
          });
        }
      });

      scrapeOneRecipe(recipesToScrape, 0, total, db, INGREDIENTS);
    });
  });
}

function scrapeIngredients() {
  if (error) return console.error('error getting flickr api: ', error);

  request('http://www.whfoods.com/foodstoc.php', function(error, response, html) {
    if (error) return console.error('error scraping ingredients: ', error);

    var $ = cheerio.load(html);
    var db = new sqlite3.Database('./app.db');
    var stmt = db.prepare("INSERT INTO ingredients VALUES (?, ?, ?, NULL)");

    $('h4').each(function(i, h4El) {
      var foodGroup = $(h4El).text();
      $(h4El).next().children().each(function(j, liEl) {
        var ingredientName = $(liEl).text();
        stmt.run(ingredientName, randomstring.generate({
          length: 12,
          charset: 'numeric'
        }), foodGroup);
      });
    });

    stmt.finalize();
    db.close();
  });
}

function updateAvatars() {
  Flickr.tokenOnly(flickrOptions, function(error, flickr) {
    var db = new sqlite3.Database('./app.db');
    var stmt = db.prepare("");
    var processed = 0;
    var total = 0;
    db.each('SELECT rowid, name FROM ingredients', function(err, row) {
      if (err) return console.error('error selecting from ingredients');

      flickr.photos.search({
        text: row.name,
        sort: 'relevance',
        per_page: 1
      }, function(err, result) {
        if (err) return console.error('error searching flickr');
        var photo = result.photos.photo[0];
        var avatarURL = util.format(
          'https://farm%s.staticflickr.com/%s/%s_%s_q.jpg',
          photo.farm, photo.server, photo.id, photo.secret
        );

        var insertStatement = util.format("UPDATE ingredients SET avatar = '%s' WHERE rowid = %s", avatarURL, row.rowid);
        console.log(insertStatement);
        db.run(insertStatement);
        processed++;
        if (processed === total) {
          db.close();
          console.log('done');
        }
      });
    }, function(error, t) {
      total = t;
    });
  });
}

scrapeRecipes();
