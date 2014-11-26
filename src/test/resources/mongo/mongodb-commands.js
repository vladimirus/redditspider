db.subreddit.find({}, {'name': 1});
db.subreddit.find({}, {'name': 1, 'updated': 1, 'subscribers': 1, _id: 0}).sort({updated: -1}).limit(1000);
db.subreddit.count({ subscribers: { $gt: 100 } });