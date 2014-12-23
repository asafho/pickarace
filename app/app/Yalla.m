//
//  Yalla.m
//  app
//
//  Created by Adi Glasman on 12/22/14.
//  Copyright (c) 2014 Asaf Horovitz. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <Simplify/SIMChargeCardViewController.h>
#import <Simplify/SIMButton.h>
#import <Simplify/UIImage+Simplify.h>
#import <Simplify/UIColor+Simplify.h>
#import <Simplify/SIMResponseViewController.h>


@interface Yalla ()<SIMChargeCardViewControllerDelegate>
@end

@implementation Yalla

-(void)testmenow
{
    //2. Create a SIMChargeViewController with your public api key
    SIMChargeCardViewController *chargeController = [[SIMChargeCardViewController alloc] initWithPublicKey:@"sbpb_ZmI0ODMyYjktZjE0MS00ZDcxLThmMGQtMDAzYzNlZmI4MDE5" primaryColor:self.primaryColor];
    
    //3. Assign your class as the delegate to the SIMChargeViewController class which takes the user input and requests a token
    chargeController.delegate = self;
    self.chargeController = chargeController;
    [self presentViewController:self.chargeController animated:YES completion:nil];
}


@end